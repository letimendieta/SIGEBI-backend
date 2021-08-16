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
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.service.MotivosConsultaService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/motivos-consulta")
public class MotivosConsultaController {

	@Autowired
	private MotivosConsultaService motivosConsultaService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public MotivosConsultaController(MotivosConsultaService motivosConsultaService) {
        this.motivosConsultaService = motivosConsultaService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<MotivosConsulta> motivosConsultaList = null;

		motivosConsultaList = motivosConsultaService.findAll();

		if( motivosConsultaList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<MotivosConsulta>>(motivosConsultaList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		MotivosConsulta motivoConsulta = null;

		motivoConsulta = motivosConsultaService.findById(id);
		
		if( motivoConsulta == null ) {
			response.put("mensaje", "El motivoConsulta con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<MotivosConsulta>(motivoConsulta, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarMotivosConsulta(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		MotivosConsulta motivoConsulta = null;
		if(!utiles.isNullOrBlank(filtros)) {
			motivoConsulta = objectMapper.readValue(filtros, MotivosConsulta.class);
		}				
		
		List<MotivosConsulta> motivosConsultaList = new ArrayList<MotivosConsulta>();
		
		if ( motivoConsulta == null ) {
			motivoConsulta = new MotivosConsulta();
		}
		if ( "-1".equals(size) ) {
			int total = motivosConsultaService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		motivosConsultaList = motivosConsultaService.buscar(fromDate, toDate, motivoConsulta, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<MotivosConsulta>>(motivosConsultaList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody MotivosConsulta motivoConsulta, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		MotivosConsulta motivoConsultaNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		motivoConsultaNew = motivosConsultaService.save(motivoConsulta);
		
		response.put("mensaje", "El motivo ha sido creado con éxito!");
		response.put("motivoConsulta", motivoConsultaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody MotivosConsulta motivoConsulta, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( motivoConsulta.getMotivoConsultaId() == null ) {
			response.put("mensaje", "Error: motivo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		MotivosConsulta motivoConsultaActual = motivosConsultaService.findById(motivoConsulta.getMotivoConsultaId());
		MotivosConsulta motivoConsultaUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( motivoConsultaActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el motivo consulta ID: "
					.concat(String.valueOf(motivoConsulta.getMotivoConsultaId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		motivoConsultaUpdated = motivosConsultaService.save(motivoConsulta);;

		response.put("mensaje", "El motivo ha sido actualizado con éxito!");
		response.put("motivoConsulta", motivoConsultaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: motivoConsulta id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		MotivosConsulta motivoConsultaActual = motivosConsultaService.findById(id);
		
		if ( motivoConsultaActual == null ) {
			response.put("mensaje", "El motivo Consulta ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		motivosConsultaService.delete(id);
		
		response.put("mensaje", "Motivo eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
