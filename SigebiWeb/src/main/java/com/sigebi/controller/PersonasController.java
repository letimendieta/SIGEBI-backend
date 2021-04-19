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
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.sigebi.entity.Carreras;
import com.sigebi.entity.Departamentos;
import com.sigebi.entity.Dependencias;
import com.sigebi.entity.Estamentos;
import com.sigebi.entity.Personas;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin
@RequestMapping("/auth/personas")
public class PersonasController {

	@Autowired
	private PersonasService personasService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public PersonasController(PersonasService personasService) {
        this.personasService = personasService;
    }
	
	@GetMapping
	public ResponseEntity<?> listar() throws SigebiException {
		List<Personas> personasList = null;
		
		personasList = personasService.listar();
		
		return new ResponseEntity<List<Personas>>(personasList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id) throws SigebiException {
		Personas persona = null;
		
		persona = personasService.obtener(id);

		return new ResponseEntity<Personas>(persona, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarPersonas(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException, DataAccessException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Personas persona = null;
		if(!utiles.isNullOrBlank(filtros)) {
			persona = objectMapper.readValue(filtros, Personas.class);
		}				
		
		List<Personas> personasList = new ArrayList<Personas>();
		
		if ( persona == null ) {
			persona = new Personas();
		}
		
		personasList = personasService.buscar(fromDate, toDate, persona, pageable);
		
        return new ResponseEntity<List<Personas>>(personasList, HttpStatus.OK);
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody Personas persona, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();		
		Personas personaNew = null;
		
		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			throw new SigebiException.BusinessException(errors.toString());
		}
		
		personaNew = personasService.guardar(persona);
		
		response.put("mensaje", "La persona ha sido creada con éxito!");
		response.put("persona", personaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping
	public ResponseEntity<?> actualizar(@Valid @RequestBody Personas persona, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
		
		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			throw new SigebiException.BusinessException(errors.toString());
		}
		
		Personas personaUpdated = null;

		personaUpdated = personasService.actualizar(persona);;

		response.put("mensaje", "La persona ha sido actualizada con éxito!");
		response.put("persona", personaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
									
		personasService.eliminar(id);

		response.put("mensaje", "Persona eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
