package com.sigebi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigebi.entity.TratamientosInsumos;
import com.sigebi.service.TratamientosInsumosService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/tratamientos-insumos")
public class TratamientosInsumosController {

	@Autowired
	private TratamientosInsumosService tratamientosInsumosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public TratamientosInsumosController(TratamientosInsumosService tratamientosInsumosService) {
        this.tratamientosInsumosService = tratamientosInsumosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<TratamientosInsumos> tratamientosInsumosList = null;

		tratamientosInsumosList = tratamientosInsumosService.findAll();

		if( tratamientosInsumosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<TratamientosInsumos>>(tratamientosInsumosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		TratamientosInsumos tratamientoInsumo = null;

		tratamientoInsumo = tratamientosInsumosService.findById(id);
		
		if( tratamientoInsumo == null ) {
			response.put("mensaje", "El tratamiento insumo con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<TratamientosInsumos>(tratamientoInsumo, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarTratamientosInsumos(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		TratamientosInsumos tratamientoInsumo = null;
		if(!utiles.isNullOrBlank(filtros)) {
			tratamientoInsumo = objectMapper.readValue(filtros, TratamientosInsumos.class);
		}				
		
		List<TratamientosInsumos> tratamientosInsumosList = new ArrayList<TratamientosInsumos>();
		
		if ( tratamientoInsumo == null ) {
			tratamientoInsumo = new TratamientosInsumos();
		}
		if ( "-1".equals(size) ) {
			int total = tratamientosInsumosService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		tratamientosInsumosList = tratamientosInsumosService.buscar(fromDate, toDate, tratamientoInsumo, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<TratamientosInsumos>>(tratamientosInsumosList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody TratamientosInsumos tratamientoInsumo, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		TratamientosInsumos tratamientoInsumoNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		tratamientoInsumoNew = tratamientosInsumosService.save(tratamientoInsumo);
		
		response.put("mensaje", "El tratamiento insumo ha sido creado con éxito!");
		response.put("tratamientoInsumo", tratamientoInsumoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody TratamientosInsumos tratamientoInsumo, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( tratamientoInsumo.getTratamientoInsumoId() == null ) {
			response.put("mensaje", "Error: tratamiento insumo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		TratamientosInsumos tratamientoInsumoActual = tratamientosInsumosService.findById(tratamientoInsumo.getTratamientoInsumoId());
		TratamientosInsumos tratamientoInsumoUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( tratamientoInsumoActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el tratamientoInsumo ID: "
					.concat(String.valueOf(tratamientoInsumo.getTratamientoInsumoId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		tratamientoInsumoUpdated = tratamientosInsumosService.save(tratamientoInsumo);;

		response.put("mensaje", "El tratamiento insumo ha sido actualizado con éxito!");
		response.put("tratamientoInsumo", tratamientoInsumoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: tratamiento insumo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		TratamientosInsumos tratamientoInsumoActual = tratamientosInsumosService.findById(id);
		
		if ( tratamientoInsumoActual == null ) {
			response.put("mensaje", "El tratamiento insumo ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}			

		tratamientosInsumosService.delete(id);
		
		response.put("mensaje", "Tratamiento insumo eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
