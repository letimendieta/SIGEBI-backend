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
import com.sigebi.entity.Dependencias;
import com.sigebi.security.service.RolService;
import com.sigebi.service.DependenciasService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Globales;
import com.sigebi.util.Mensaje;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/dependencias")
public class DependenciasController {

	@Autowired
	private DependenciasService dependenciasService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	private RolService rolService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public DependenciasController(DependenciasService dependenciasService) {
        this.dependenciasService = dependenciasService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Dependencias> dependenciasList = null;
		
		dependenciasList = dependenciasService.findAll();

		if( dependenciasList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Dependencias>>(dependenciasList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Dependencias dependencia = null;
		
		dependencia = dependenciasService.findById(id);
		
		if( dependencia == null ) {
			response.put("mensaje", "La dependencia con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Dependencias>(dependencia, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarDependencias(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Dependencias dependencia = null;
		if(!utiles.isNullOrBlank(filtros)) {
			dependencia = objectMapper.readValue(filtros, Dependencias.class);
		}				
		
		List<Dependencias> dependenciasList = new ArrayList<Dependencias>();
		
		if ( dependencia == null ) {
			dependencia = new Dependencias();
		}
		
		if ("-1".equals(size)) {
			int total = dependenciasService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		dependenciasList = dependenciasService.buscar(fromDate, toDate, dependencia, orderBy, orderDir, pageable);		
		
        return new ResponseEntity<List<Dependencias>>(dependenciasList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Dependencias dependencia, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		Dependencias dependenciaNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		dependenciaNew = dependenciasService.save(dependencia);
		
		response.put("mensaje", "La dependencia ha sido creada con éxito!");
		response.put("dependencia", dependenciaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Dependencias dependencia, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( dependencia.getDependenciaId() == null ) {
			response.put("mensaje", "Error: dependencia id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Dependencias dependenciaActual = dependenciasService.findById(dependencia.getDependenciaId());
		Dependencias dependenciaUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( dependenciaActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, la dependencia ID: "
					.concat(String.valueOf(dependencia.getDependenciaId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		dependenciaUpdated = dependenciasService.save(dependencia);;

		response.put("mensaje", "La dependencia ha sido actualizada con éxito!");
		response.put("dependencia", dependenciaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if( !rolService.verificarRol(Globales.ROL_ABM_CONFIGURACION) ){
			return new ResponseEntity(new Mensaje("No cuenta con el rol requerido "), HttpStatus.UNAUTHORIZED);
		}
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: dependencia id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Dependencias dependenciaActual = dependenciasService.findById(id);
		
		if ( dependenciaActual == null ) {
			response.put("mensaje", "La dependencia ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		dependenciasService.delete(id);
		
		response.put("mensaje", "Dependencia eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
