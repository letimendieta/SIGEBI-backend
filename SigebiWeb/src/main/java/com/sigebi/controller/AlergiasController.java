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
import com.sigebi.entity.Alergias;
import com.sigebi.service.AlergiasService;
import com.sigebi.service.FilesStorageService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/alergias")
public class AlergiasController {

	@Autowired
	private AlergiasService alergiasService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	FilesStorageService storageService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public AlergiasController(AlergiasService alergiasService) {
        this.alergiasService = alergiasService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Alergias> alergiaList = null;
		
		alergiaList = alergiasService.findAll();
		
		if( alergiaList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Alergias>>(alergiaList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Alergias alergia = null;
		
		alergia = alergiasService.findById(id);
		
		if( alergia == null ) {
			response.put("mensaje", "El alergia con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Alergias>(alergia, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarAlergias(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{		
		
		ObjectMapper objectMapper = new ObjectMapper();
					
		Alergias alergia = null;
		if(!utiles.isNullOrBlank(filtros)) {
			alergia = objectMapper.readValue(filtros, Alergias.class);
		}
		
		List<Alergias> alergiaList = new ArrayList<Alergias>();
		
		if ( alergia == null ) {
			alergia = new Alergias();
		}
		
		if ( "-1".equals(size) ) {
			int total = alergiasService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		alergiaList = alergiasService.buscar(fromDate, toDate, alergia, orderBy, orderDir, pageable);
							
	    return new ResponseEntity<List<Alergias>>(alergiaList, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Alergias alergia, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();		
		Alergias alergiaNew = null;
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		alergiaNew = alergiasService.guardar(alergia);
		
		response.put("mensaje", "La alergia ha sido creado con éxito!");
		response.put("alergia", alergiaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Alergias alergia, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( alergia.getAlergiaId() == null ) {
			response.put("mensaje", "Error: alergia id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Alergias alergiaActual = alergiasService.findById(alergia.getAlergiaId());
		Alergias alergiaUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( alergiaActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, la alergia ID: "
					.concat(String.valueOf(alergia.getAlergiaId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		alergiaUpdated = alergiasService.actualizar(alergia);;

		response.put("mensaje", "La alergia ha sido actualizada con éxito!");
		response.put("alergia", alergiaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: alergia id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Alergias alergiaActual = alergiasService.findById(id);
		
		if ( alergiaActual == null ) {
			response.put("mensaje", "La alergia ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		alergiasService.delete(id);
		
		response.put("mensaje", "Alergias eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
