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
import com.sigebi.entity.Areas;
import com.sigebi.security.service.RolService;
import com.sigebi.service.AreasService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Globales;
import com.sigebi.util.Mensaje;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/areas")
public class AreasController {

	@Autowired
	private AreasService areasService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	private RolService rolService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public AreasController(AreasService areasService) {
        this.areasService = areasService;
    }

	@GetMapping
	public ResponseEntity<?> listar() throws SigebiException {
		List<Areas> areasList = null;
		
		areasList = areasService.listar();
		
		return new ResponseEntity<List<Areas>>(areasList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id) throws SigebiException {
		Areas area = null;
		
		area = areasService.obtener(id);
					
		return new ResponseEntity<Areas>(area, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscar(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException, DataAccessException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Areas area = null;
		if(!utiles.isNullOrBlank(filtros)) {
			area = objectMapper.readValue(filtros, Areas.class);
		}				
		
		List<Areas> areasList = new ArrayList<Areas>();
		
		if ( area == null ) {
			area = new Areas();
		}
		if ( "-1".equals(size) ) {
			int total = areasService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		areasList = areasService.buscar(fromDate, toDate, area, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<Areas>>(areasList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody Areas area, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();		
		Areas areaNew = null;
		
		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());			
			throw new SigebiException.BusinessException(errors.toString());
		}
		
		areaNew = areasService.guardar(area);
		
		response.put("mensaje", "El área ha sido creado con éxito!");
		response.put("area", areaNew);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> actualizar(@Valid @RequestBody Areas area, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
				
		Areas areaUpdated = null;

		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());			
			response.put("errors", errors);
			throw new SigebiException.BusinessException(errors.toString());
		}
		
		areaUpdated = areasService.actualizar(area);;

		response.put("mensaje", "El área ha sido actualizado con éxito!");
		response.put("area", areaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> borrar(@PathVariable int id) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
		
		if( !rolService.verificarRol(Globales.ROL_ABM_CONFIGURACION) ){
			return new ResponseEntity(new Mensaje("No cuenta con el rol requerido "), HttpStatus.UNAUTHORIZED);
		}
		
		areasService.eliminar(id);
		
		response.put("mensaje", "Área eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
