package com.sigebi.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import com.sigebi.entity.Insumos;
import com.sigebi.service.InsumosService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/insumos")
public class InsumosController {

	@Autowired
	private InsumosService insumosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public InsumosController(InsumosService insumosService) {
        this.insumosService = insumosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Insumos> insumosList = null;
		try {
			insumosList = insumosService.findAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if( insumosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Insumos>>(insumosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Insumos insumo = null;
		try {
			insumo = insumosService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if( insumo == null ) {
			response.put("mensaje", "El insumo con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Insumos>(insumo, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarInsumos(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JSONObject jo = new JSONObject(filtros);
		String fechaString = jo.length()>0 && !jo.get("fechaVencimiento").equals(null) ? (String) jo.get("fechaVencimiento") : "";
		LocalDate fecha = null;
		
		//se quita fecha de filtros por que da error al mapear
		if(!fechaString.equals(null) && !fechaString.equals("")) {
			String fechaAquitar = '"' + (String) jo.get("fechaVencimiento") + '"';
			filtros = filtros.replace(fechaAquitar, "null");			
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			fecha = LocalDate.parse(fechaString, format);					
		}
		
		Insumos insumo = null;
		if(!utiles.isNullOrBlank(filtros)) {
			insumo = objectMapper.readValue(filtros, Insumos.class);
			insumo.setFechaVencimiento(fecha);
		}				
		
		Map<String, Object> response = new HashMap<>();
		List<Insumos> insumosList = new ArrayList<Insumos>();
		
		if ( insumo == null ) {
			insumo = new Insumos();
		}
		try {
			insumosList = insumosService.buscar(fromDate, toDate, insumo, pageable);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
        return new ResponseEntity<List<Insumos>>(insumosList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Insumos insumo, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		Insumos insumoNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			insumoNew = insumosService.save(insumo);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al guardar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El insumo ha sido creada con éxito!");
		response.put("insumo", insumoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Insumos insumo, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( insumo.getInsumoId() == null ) {
			response.put("mensaje", "Error: insumo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Insumos insumoActual = insumosService.findById(insumo.getInsumoId());
		Insumos insumoUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( insumoActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el insumo ID: "
					.concat(String.valueOf(insumo.getInsumoId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			insumoUpdated = insumosService.save(insumo);;

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el insumo en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El insumo ha sido actualizada con éxito!");
		response.put("insumo", insumoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: insumo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Insumos insumoActual = insumosService.findById(id);
		
		if ( insumoActual == null ) {
			response.put("mensaje", "La insumo ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		try {
			insumosService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el insumo de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Insumo eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
