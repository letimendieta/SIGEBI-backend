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
import com.sigebi.entity.Preguntas;
import com.sigebi.service.PreguntasService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/preguntas")
public class PreguntasController {

	@Autowired
	private PreguntasService preguntasService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public PreguntasController(PreguntasService preguntasService) {
        this.preguntasService = preguntasService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Preguntas> preguntasList = null;

		preguntasList = preguntasService.findAll();

		if( preguntasList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Preguntas>>(preguntasList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Preguntas pregunta = null;

		pregunta = preguntasService.findById(id);
		
		if( pregunta == null ) {
			response.put("mensaje", "La pregunta con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Preguntas>(pregunta, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarPreguntas(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Preguntas pregunta = null;
		if(!utiles.isNullOrBlank(filtros)) {
			pregunta = objectMapper.readValue(filtros, Preguntas.class);
		}				
		
		List<Preguntas> preguntasList = new ArrayList<Preguntas>();
		
		if ( pregunta == null ) {
			pregunta = new Preguntas();
		}
		if ( "-1".equals(size) ) {
			int total = preguntasService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		preguntasList = preguntasService.buscar(fromDate, toDate, pregunta, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<Preguntas>>(preguntasList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Preguntas pregunta, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		Preguntas preguntaNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		preguntaNew = preguntasService.save(pregunta);
		
		response.put("mensaje", "La pregunta ha sido creada con éxito!");
		response.put("pregunta", preguntaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Preguntas pregunta, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( pregunta.getPreguntaId() == null ) {
			response.put("mensaje", "Error: pregunta id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Preguntas preguntaActual = preguntasService.findById(pregunta.getPreguntaId());
		Preguntas preguntaUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( preguntaActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el pregunta ID: "
					.concat(String.valueOf(pregunta.getPreguntaId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		preguntaUpdated = preguntasService.save(pregunta);;

		response.put("mensaje", "La pregunta ha sido actualizada con éxito!");
		response.put("pregunta", preguntaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: pregunta id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Preguntas preguntaActual = preguntasService.findById(id);
		
		if ( preguntaActual == null ) {
			response.put("mensaje", "La pregunta ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		preguntasService.delete(id);
		
		response.put("mensaje", "Pregunta eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
