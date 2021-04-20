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
import com.sigebi.entity.Antecedentes;
import com.sigebi.service.AntecedentesService;
import com.sigebi.service.FilesStorageService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/antecedentes")
public class AntecedentesController {

	@Autowired
	private AntecedentesService antecedentesService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	FilesStorageService storageService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public AntecedentesController(AntecedentesService antecedentesService) {
        this.antecedentesService = antecedentesService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Antecedentes> antecedenteList = null;
		
		antecedenteList = antecedentesService.findAll();

		if( antecedenteList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Antecedentes>>(antecedenteList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Antecedentes antecedente = null;
		
		antecedente = antecedentesService.findById(id);
		
		if( antecedente == null ) {
			response.put("mensaje", "El antecedente con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Antecedentes>(antecedente, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarAntecedentes(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{		
		
		ObjectMapper objectMapper = new ObjectMapper();
					
		Antecedentes antecedente = null;
		if(!utiles.isNullOrBlank(filtros)) {
			antecedente = objectMapper.readValue(filtros, Antecedentes.class);
		}
		
		Map<String, Object> response = new HashMap<>();
		List<Antecedentes> antecedenteList = new ArrayList<Antecedentes>();
		
		if ( antecedente == null ) {
			antecedente = new Antecedentes();
		}

		antecedenteList = antecedentesService.buscar(fromDate, toDate, antecedente, pageable);
							
	    return new ResponseEntity<List<Antecedentes>>(antecedenteList, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Antecedentes antecedente, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();		
		Antecedentes antecedenteNew = null;
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		antecedenteNew = antecedentesService.guardar(antecedente);
		
		response.put("mensaje", "El antecedente ha sido creado con éxito!");
		response.put("antecedente", antecedenteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Antecedentes antecedente, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( antecedente.getAntecedenteId() == null ) {
			response.put("mensaje", "Error: antecedente id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Antecedentes antecedenteActual = antecedentesService.findById(antecedente.getAntecedenteId());
		Antecedentes antecedenteUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( antecedenteActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el antecedente ID: "
					.concat(String.valueOf(antecedente.getAntecedenteId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		antecedenteUpdated = antecedentesService.actualizar(antecedente);;

		response.put("mensaje", "El antecedente ha sido actualizado con éxito!");
		response.put("antecedente", antecedenteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: antecedente id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Antecedentes antecedenteActual = antecedentesService.findById(id);
		
		if ( antecedenteActual == null ) {
			response.put("mensaje", "El antecedente ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		antecedentesService.delete(id);
		
		response.put("mensaje", "Antecedentes eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
