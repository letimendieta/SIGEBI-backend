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
import com.sigebi.entity.Vacunaciones;
import com.sigebi.service.UtilesService;
import com.sigebi.service.VacunacionesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/vacunaciones")
public class VacunacionesController {

	@Autowired
	private VacunacionesService vacunacionesService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public VacunacionesController(VacunacionesService vacunacionesService) {
        this.vacunacionesService = vacunacionesService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Vacunaciones> vacunacionesList = null;

		vacunacionesList = vacunacionesService.findAll();

		if( vacunacionesList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Vacunaciones>>(vacunacionesList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Vacunaciones vacunaciones = null;

		vacunaciones = vacunacionesService.findById(id);
		
		if( vacunaciones == null ) {
			response.put("mensaje", "Vacunación con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Vacunaciones>(vacunaciones, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarVacunaciones(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Vacunaciones vacunaciones = null;
		if(!utiles.isNullOrBlank(filtros)) {
			vacunaciones = objectMapper.readValue(filtros, Vacunaciones.class);
		}				
		
		List<Vacunaciones> vacunacionesList = new ArrayList<Vacunaciones>();
		
		if ( vacunaciones == null ) {
			vacunaciones = new Vacunaciones();
		}
		if ( "-1".equals(size) ) {
			int total = vacunacionesService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		vacunacionesList = vacunacionesService.buscar(fromDate, toDate, vacunaciones, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<Vacunaciones>>(vacunacionesList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Vacunaciones vacunaciones, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		Vacunaciones vacunacionesNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}		

		vacunacionesNew = vacunacionesService.save(vacunaciones);
		
		response.put("mensaje", "Vacunación ha sido creada con éxito!");
		response.put("vacunaciones", vacunacionesNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Vacunaciones vacunaciones, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( vacunaciones.getVacunacionId() == null ) {
			response.put("mensaje", "Error: vacunaciones id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Vacunaciones vacunacionesActual = vacunacionesService.findById(vacunaciones.getVacunacionId());
		Vacunaciones vacunacionesUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( vacunacionesActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, la vacunación ID: "
					.concat(String.valueOf(vacunaciones.getVacunacionId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		vacunacionesUpdated = vacunacionesService.save(vacunaciones);;

		response.put("mensaje", "Vacunación ha sido actualizada con éxito!");
		response.put("vacunaciones", vacunacionesUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: vacunacion id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Vacunaciones vacunacionesActual = vacunacionesService.findById(id);
		
		if ( vacunacionesActual == null ) {
			response.put("mensaje", "La vacunación ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		vacunacionesService.delete(id);
		
		response.put("mensaje", "Vacunación eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
