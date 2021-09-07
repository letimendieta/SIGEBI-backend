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
import com.sigebi.entity.PreguntasHistorial;
import com.sigebi.service.PreguntasHistorialService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/preguntas-historial")
public class PreguntasHistorialController {

	@Autowired
	private PreguntasHistorialService preguntasService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public PreguntasHistorialController(PreguntasHistorialService preguntasService) {
        this.preguntasService = preguntasService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<PreguntasHistorial> preguntasList = null;

		preguntasList = preguntasService.findAll();

		if( preguntasList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<PreguntasHistorial>>(preguntasList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		PreguntasHistorial preguntaHistorial = null;

		preguntaHistorial = preguntasService.findById(id);
		
		if( preguntaHistorial == null ) {
			response.put("mensaje", "Pregunta historial con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<PreguntasHistorial>(preguntaHistorial, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarPreguntasHistorial(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		PreguntasHistorial preguntaHistorial = null;
		if(!utiles.isNullOrBlank(filtros)) {
			preguntaHistorial = objectMapper.readValue(filtros, PreguntasHistorial.class);
		}				
		
		List<PreguntasHistorial> preguntasList = new ArrayList<PreguntasHistorial>();
		
		if ( preguntaHistorial == null ) {
			preguntaHistorial = new PreguntasHistorial();
		}
		if ( "-1".equals(size) ) {
			int total = preguntasService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			

		preguntasList = preguntasService.buscar(fromDate, toDate, preguntaHistorial, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<PreguntasHistorial>>(preguntasList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody PreguntasHistorial preguntaHistorial, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		PreguntasHistorial preguntaHistorialNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		preguntaHistorialNew = preguntasService.save(preguntaHistorial);
		
		response.put("mensaje", "Pregunta historial ha sido creado con éxito!");
		response.put("preguntaHistorial", preguntaHistorialNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody PreguntasHistorial preguntaHistorial, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		PreguntasHistorial preguntaHistorialActual = preguntasService.findById(preguntaHistorial.getPreguntaHistorialId());
		PreguntasHistorial preguntaHistorialUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( preguntaHistorialActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el ID: "
					.concat(String.valueOf(preguntaHistorial.getPreguntaHistorialId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		preguntaHistorialUpdated = preguntasService.save(preguntaHistorial);;

		response.put("mensaje", "Pregunta historial ha sido actualizada con éxito!");
		response.put("preguntaHistorial", preguntaHistorialUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: pregunta historial id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		PreguntasHistorial preguntaHistorialActual = preguntasService.findById(id);
		
		if ( preguntaHistorialActual == null ) {
			response.put("mensaje", "Pregunta historial ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		preguntasService.delete(id);
		
		response.put("mensaje", "Pregunta historial eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
