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
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.security.service.RolService;
import com.sigebi.service.EnfermedadesCie10Service;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Globales;
import com.sigebi.util.Mensaje;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/enfermedades-cie10")
public class EnfermedadesCie10Controller {

	@Autowired
	private EnfermedadesCie10Service enfermedadesCie10Service;
	@Autowired
	private UtilesService utiles;
	@Autowired
	private RolService rolService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public EnfermedadesCie10Controller(EnfermedadesCie10Service enfermedadesCie10Service) {
        this.enfermedadesCie10Service = enfermedadesCie10Service;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<EnfermedadesCie10> enfermedadesCie10List = null;
		
		enfermedadesCie10List = enfermedadesCie10Service.findAll();

		if( enfermedadesCie10List.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<EnfermedadesCie10>>(enfermedadesCie10List, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		EnfermedadesCie10 enfermedadCie10 = null;
		
		enfermedadCie10 = enfermedadesCie10Service.findById(id);
		
		if( enfermedadCie10 == null ) {
			response.put("mensaje", "La enfermedad con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<EnfermedadesCie10>(enfermedadCie10, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarEnfermedadesCie10(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		EnfermedadesCie10 enfermedadCie10 = null;
		if(!utiles.isNullOrBlank(filtros)) {
			enfermedadCie10 = objectMapper.readValue(filtros, EnfermedadesCie10.class);
		}				
		
		List<EnfermedadesCie10> enfermedadesCie10List = new ArrayList<EnfermedadesCie10>();
		
		if ( enfermedadCie10 == null ) {
			enfermedadCie10 = new EnfermedadesCie10();
		}
		if ( "-1".equals(size) ) {
			int total = enfermedadesCie10Service.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		enfermedadesCie10List = enfermedadesCie10Service.buscar(fromDate, toDate, enfermedadCie10, orderBy, orderDir, pageable);	
		
        return new ResponseEntity<List<EnfermedadesCie10>>(enfermedadesCie10List, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody EnfermedadesCie10 enfermedadCie10, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		EnfermedadesCie10 enfermedadCie10New = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		enfermedadCie10New = enfermedadesCie10Service.save(enfermedadCie10);
		
		response.put("mensaje", "La enfermedad ha sido creada con éxito!");
		response.put("enfermedadCie10", enfermedadCie10New);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody EnfermedadesCie10 enfermedadCie10, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( enfermedadCie10.getEnfermedadCie10Id() == null ) {
			response.put("mensaje", "Error: enfermedad id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		EnfermedadesCie10 enfermedadCie10Actual = enfermedadesCie10Service.findById(enfermedadCie10.getEnfermedadCie10Id());
		EnfermedadesCie10 enfermedadCie10Updated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( enfermedadCie10Actual == null ) {
			response.put("mensaje", "Error: no se pudo editar, la enfermedad ID: "
					.concat(String.valueOf(enfermedadCie10.getEnfermedadCie10Id()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		enfermedadCie10Updated = enfermedadesCie10Service.save(enfermedadCie10);;

		response.put("mensaje", "La enfermedad ha sido actualizada con éxito!");
		response.put("enfermedadCie10", enfermedadCie10Updated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
				
		if( !rolService.verificarRol(Globales.ROL_ABM_CONFIGURACION) ){
			return new ResponseEntity(new Mensaje("No cuenta con el rol requerido "), HttpStatus.UNAUTHORIZED);
		}
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: enfermedad id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		EnfermedadesCie10 enfermedadCie10Actual = enfermedadesCie10Service.findById(id);
		
		if ( enfermedadCie10Actual == null ) {
			response.put("mensaje", "La enfermedad ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		enfermedadesCie10Service.delete(id);
		
		response.put("mensaje", "Enfermedad eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
