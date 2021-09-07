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
import com.sigebi.entity.Anamnesis;
import com.sigebi.service.AnamnesisService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/anamnesis")
public class AnamnesisController {

	@Autowired
	private AnamnesisService anamnesisService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public AnamnesisController(AnamnesisService anamnesisService) {
        this.anamnesisService = anamnesisService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Anamnesis> anamnesisList = null;
		
		anamnesisList = anamnesisService.findAll();

		if( anamnesisList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Anamnesis>>(anamnesisList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Anamnesis anamnesis = null;
		
		anamnesis = anamnesisService.findById(id);
		
		if( anamnesis == null ) {
			response.put("mensaje", "La anamnesis con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Anamnesis>(anamnesis, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarAnamnesis(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Anamnesis anamnesis = null;
		if(!utiles.isNullOrBlank(filtros)) {
			anamnesis = objectMapper.readValue(filtros, Anamnesis.class);
		}				
		
		List<Anamnesis> anamnesisList = new ArrayList<Anamnesis>();
		
		if ( anamnesis == null ) {
			anamnesis = new Anamnesis();
		}
		if ( "-1".equals(size) ) {
			int total = anamnesisService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		anamnesisList = anamnesisService.buscar(fromDate, toDate, anamnesis, orderBy, orderDir, pageable);	
		
        return new ResponseEntity<List<Anamnesis>>(anamnesisList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Anamnesis anamnesis, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		Anamnesis anamnesisNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(anamnesis.getPacienteId() == null) {
			response.put("errors", "El pacienteid es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
		
		anamnesisNew = anamnesisService.save(anamnesis);
		
		response.put("mensaje", "La anamnesis ha sido creada con éxito!");
		response.put("anamnesis", anamnesisNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Anamnesis anamnesis, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( anamnesis.getAnamnesisId() == null ) {
			response.put("mensaje", "Error: anamnesis id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Anamnesis anamnesisActual = anamnesisService.findById(anamnesis.getAnamnesisId());
		Anamnesis anamnesisUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( anamnesisActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el anamnesis ID: "
					.concat(String.valueOf(anamnesis.getAnamnesisId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		anamnesisUpdated = anamnesisService.save(anamnesis);;

		response.put("mensaje", "La anamnesis ha sido actualizada con éxito!");
		response.put("anamnesis", anamnesisUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: anamnesis id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Anamnesis anamnesisActual = anamnesisService.findById(id);
		
		if ( anamnesisActual == null ) {
			response.put("mensaje", "La anamnesis ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		anamnesisService.delete(id);
		
		response.put("mensaje", "Anamnesis eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
