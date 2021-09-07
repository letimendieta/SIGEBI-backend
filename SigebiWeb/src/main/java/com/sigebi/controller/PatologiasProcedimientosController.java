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
import com.sigebi.entity.PatologiasProcedimientos;
import com.sigebi.service.PatologiasProcedimientosService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/patologias-procedimientos")
public class PatologiasProcedimientosController {

	@Autowired
	private PatologiasProcedimientosService patologiasProcedimientosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public PatologiasProcedimientosController(PatologiasProcedimientosService patologiasProcedimientosService) {
        this.patologiasProcedimientosService = patologiasProcedimientosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<PatologiasProcedimientos> patologiasProcedimientosList = null;

		patologiasProcedimientosList = patologiasProcedimientosService.findAll();

		if( patologiasProcedimientosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<PatologiasProcedimientos>>(patologiasProcedimientosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		PatologiasProcedimientos patologiaProcedimiento = null;
		
		patologiaProcedimiento = patologiasProcedimientosService.findById(id);
		
		if( patologiaProcedimiento == null ) {
			response.put("mensaje", "El patologia procedimiento con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<PatologiasProcedimientos>(patologiaProcedimiento, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarPatologiasProcedimientos(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		PatologiasProcedimientos patologiaProcedimiento = null;
		if(!utiles.isNullOrBlank(filtros)) {
			patologiaProcedimiento = objectMapper.readValue(filtros, PatologiasProcedimientos.class);
		}				
		
		List<PatologiasProcedimientos> patologiasProcedimientosList = new ArrayList<PatologiasProcedimientos>();
		
		if ( patologiaProcedimiento == null ) {
			patologiaProcedimiento = new PatologiasProcedimientos();
		}
		if ( "-1".equals(size) ) {
			int total = patologiasProcedimientosService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		patologiasProcedimientosList = patologiasProcedimientosService.buscar(fromDate, toDate, patologiaProcedimiento, orderBy, orderDir, pageable);	
		
        return new ResponseEntity<List<PatologiasProcedimientos>>(patologiasProcedimientosList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody PatologiasProcedimientos patologiaProcedimiento, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		PatologiasProcedimientos patologiaProcedimientoNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		patologiaProcedimientoNew = patologiasProcedimientosService.save(patologiaProcedimiento);
		
		response.put("mensaje", "Patología procedimiento ha sido creado con éxito!");
		response.put("patologiaProcedimiento", patologiaProcedimientoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody PatologiasProcedimientos patologiaProcedimiento, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( patologiaProcedimiento.getPatologiaProcedimientoId() == null ) {
			response.put("mensaje", "Error: patología procedimiento id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		PatologiasProcedimientos patologiaProcedimientoActual = patologiasProcedimientosService.findById(patologiaProcedimiento.getPatologiaProcedimientoId());
		PatologiasProcedimientos patologiaProcedimientoUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( patologiaProcedimientoActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el patologiaProcedimiento ID: "
					.concat(String.valueOf(patologiaProcedimiento.getPatologiaProcedimientoId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		patologiaProcedimientoUpdated = patologiasProcedimientosService.save(patologiaProcedimiento);;

		response.put("mensaje", "Patologia procedimiento ha sido actualizado con éxito!");
		response.put("patologiaProcedimiento", patologiaProcedimientoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: patología procedimiento id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		PatologiasProcedimientos patologiaProcedimientoActual = patologiasProcedimientosService.findById(id);
		
		if ( patologiaProcedimientoActual == null ) {
			response.put("mensaje", "Patología procedimiento ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		patologiasProcedimientosService.delete(id);
		
		response.put("mensaje", "Patologia procedimiento eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
