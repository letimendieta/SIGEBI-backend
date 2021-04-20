package com.sigebi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import com.sigebi.entity.ProcedimientosInsumos;
import com.sigebi.service.ProcedimientosInsumosService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/procedimientos-insumos")
public class ProcedimientosInsumosController {

	@Autowired
	private ProcedimientosInsumosService procedimientosInsumosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public ProcedimientosInsumosController(ProcedimientosInsumosService procedimientosInsumosService) {
        this.procedimientosInsumosService = procedimientosInsumosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<ProcedimientosInsumos> procedimientosInsumosList = null;

		procedimientosInsumosList = procedimientosInsumosService.findAll();

		if( procedimientosInsumosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<ProcedimientosInsumos>>(procedimientosInsumosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		ProcedimientosInsumos procedimientoInsumo = null;

		procedimientoInsumo = procedimientosInsumosService.findById(id);
		
		if( procedimientoInsumo == null ) {
			response.put("mensaje", "El procedimiento insumo con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<ProcedimientosInsumos>(procedimientoInsumo, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarProcedimientosInsumos(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ProcedimientosInsumos procedimientoInsumo = null;
		if(!utiles.isNullOrBlank(filtros)) {
			procedimientoInsumo = objectMapper.readValue(filtros, ProcedimientosInsumos.class);
		}				
		
		Map<String, Object> response = new HashMap<>();
		List<ProcedimientosInsumos> procedimientosInsumosList = new ArrayList<ProcedimientosInsumos>();
		
		if ( procedimientoInsumo == null ) {
			procedimientoInsumo = new ProcedimientosInsumos();
		}
		if ( "-1".equals(size) ) {
			int total = procedimientosInsumosService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			

		procedimientosInsumosList = procedimientosInsumosService.buscar(fromDate, toDate, procedimientoInsumo, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<ProcedimientosInsumos>>(procedimientosInsumosList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody ProcedimientosInsumos procedimientoInsumo, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		ProcedimientosInsumos procedimientoInsumoNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		procedimientoInsumoNew = procedimientosInsumosService.save(procedimientoInsumo);
		
		response.put("mensaje", "El procedimiento insumo ha sido creada con éxito!");
		response.put("procedimientoInsumo", procedimientoInsumoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody ProcedimientosInsumos procedimientoInsumo, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( procedimientoInsumo.getProcedimientoInsumoId() == null ) {
			response.put("mensaje", "Error: procedimiento insumo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		ProcedimientosInsumos procedimientoInsumoActual = procedimientosInsumosService.findById(procedimientoInsumo.getProcedimientoInsumoId());
		ProcedimientosInsumos procedimientoInsumoUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( procedimientoInsumoActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el procedimiento insumo ID: "
					.concat(String.valueOf(procedimientoInsumo.getProcedimientoInsumoId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		procedimientoInsumoUpdated = procedimientosInsumosService.save(procedimientoInsumo);;

		response.put("mensaje", "El procedimiento insumo ha sido actualizada con éxito!");
		response.put("procedimientoInsumo", procedimientoInsumoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: procedimiento insumo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		ProcedimientosInsumos procedimientoInsumoActual = procedimientosInsumosService.findById(id);
		
		if ( procedimientoInsumoActual == null ) {
			response.put("mensaje", "El procedimiento insumo ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		procedimientosInsumosService.delete(id);
		
		response.put("mensaje", "procedimiento insumo eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
