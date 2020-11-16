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
import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.service.FilesStorageService;
import com.sigebi.service.HistorialesClinicosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/historial-Clinico")
public class HistorialClinicoController {

	@Autowired
	private HistorialesClinicosService historialesClinicosService;
	@Autowired
	private PacientesService pacientesService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	FilesStorageService storageService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public HistorialClinicoController(HistorialesClinicosService historialesClinicosService) {
        this.historialesClinicosService = historialesClinicosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<HistorialClinico> historialClinicosList = null;
		try {
			historialClinicosList = historialesClinicosService.findAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if( historialClinicosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		HistorialClinico historialClinico = null;
		try {
			historialClinico = historialesClinicosService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if( historialClinico == null ) {
			response.put("mensaje", "El historialClinico con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<HistorialClinico>(historialClinico, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarHistorialClinico(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{		
		
		ObjectMapper objectMapper = new ObjectMapper();
					
		HistorialClinico historialClinico = null;
		Pacientes pacientes = null;
		
		if(!utiles.isNullOrBlank(filtros)) {
			historialClinico = objectMapper.readValue(filtros, HistorialClinico.class);
		}
		/*if(!utiles.isNullOrBlank(filtros)) {
			pacientes = objectMapper.readValue(filtros, Pacientes.class);
		}*/
		Map<String, Object> response = new HashMap<>();
		List<HistorialClinico> historialClinicosList = new ArrayList<HistorialClinico>();
		
		if ( historialClinico == null ) {
			historialClinico = new HistorialClinico();
		}
		
		List<Personas> personasList = new ArrayList<Personas>();
		List<Integer> personasId = new ArrayList<Integer>();
						
		List<Pacientes> pacientesList = new ArrayList<Pacientes>();
		List<Integer> pacientesIds = new ArrayList<Integer>();
		
		/*if( historialClinico.getPacientes() != null) {
			try {
				personasId = new ArrayList<Integer>();
				personasList = new ArrayList<Personas>();
				
				if(historialClinico.getPacientes().getPersonas() != null) {
					personasList = personasService.buscar(null, null, historialClinico.getPacientes().getPersonas(), PageRequest.of(0, 20));
					
					for( Personas persona : personasList ){
						personasId.add(persona.getPersonaId());
					}
					
					//Si se reciben datos de persona y si no se encuentra, retornar vacio
					if(personasList.isEmpty()) {
						return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
					}
				}
				pacientesList = pacientesService.buscar(null, null, historialClinico.getPacientes(), personasId, PageRequest.of(0, 20));
				
				//Si se reciben datos de paciente y si no se encuentra, retornar vacio
				if(pacientesList.isEmpty()) {
					return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
				}					
				
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta de los datos del paciente");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			for( Pacientes paciente : pacientesList ){
				pacientesIds.add(paciente.getPacienteId());
			}
		}*/
		
		try {
			historialClinicosList = historialesClinicosService.buscar(fromDate, toDate, historialClinico, 
																pacientesIds, pageable);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta de los datos del historialClinico");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
						
        return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody HistorialClinico historialClinico, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();		
		HistorialClinico historialClinicoNew = null;
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		try {
			historialClinicoNew = historialesClinicosService.guardar(historialClinico);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al guardar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El historialClinico ha sido creado con éxito!");
		response.put("historialClinico", historialClinicoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody HistorialClinico historialClinico, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( historialClinico.getHistorialClinicoId() == null ) {
			response.put("mensaje", "Error: historialClinico id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		HistorialClinico historialClinicoActual = historialesClinicosService.findById(historialClinico.getHistorialClinicoId());
		HistorialClinico historialClinicoUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( historialClinicoActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el historialClinico ID: "
					.concat(String.valueOf(historialClinico.getHistorialClinicoId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			historialClinicoUpdated = historialesClinicosService.actualizar(historialClinico);;

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el historialClinico en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El historialClinico ha sido actualizado con éxito!");
		response.put("historialClinico", historialClinicoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: historialClinico id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		HistorialClinico historialClinicoActual = historialesClinicosService.findById(id);
		
		if ( historialClinicoActual == null ) {
			response.put("mensaje", "El historialClinico ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		try {
			historialesClinicosService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el historialClinico de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "HistorialClinico eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
