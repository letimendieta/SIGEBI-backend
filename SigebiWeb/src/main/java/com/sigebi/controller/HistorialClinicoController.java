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
import com.sigebi.entity.HistorialClinicoPaciente;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.service.FilesStorageService;
import com.sigebi.service.HistorialesClinicosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/historial-clinico")
public class HistorialClinicoController {

	@Autowired
	private HistorialesClinicosService historialesClinicosService;
	@Autowired
	private PacientesService pacientesService;
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
		
		HistorialClinicoPaciente busqueda = null;
		HistorialClinico historialClinico = null;
		Pacientes paciente = null;
		List<HistorialClinicoPaciente> busquedaList = new ArrayList<HistorialClinicoPaciente>();
		
		if(!utiles.isNullOrBlank(filtros)) {
			busqueda = objectMapper.readValue(filtros, HistorialClinicoPaciente.class);
		}
		historialClinico = busqueda.getHistorialClinico();
		paciente = busqueda.getPaciente();

		Map<String, Object> response = new HashMap<>();
		List<HistorialClinico> historialClinicosList = new ArrayList<HistorialClinico>();
		
		if ( historialClinico == null ) {
			historialClinico = new HistorialClinico();
		}
		
		Pacientes pacienteDb = new Pacientes();
		
		if( paciente != null && paciente.getPacienteId() != null) {
			try {
				
				pacienteDb = pacientesService.findById(paciente.getPacienteId());
				
				//Si se reciben datos de paciente y si no se encuentra, retornar vacio
				if(pacienteDb == null) {
					return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
				}
				//Si el paciente no tiene historial clinico retornar vacio
				if(pacienteDb.getHistorialClinico() == null) {
					return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
				}
				
				historialClinico.setHistorialClinicoId(pacienteDb.getHistorialClinico().getHistorialClinicoId());				
				
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta de los datos del paciente");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		try {
			historialClinicosList = historialesClinicosService.buscar(fromDate, toDate, historialClinico, 
																null, pageable);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta de los datos del historialClinico");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		for( HistorialClinico historialClin : historialClinicosList ){
			HistorialClinicoPaciente busquedaHp = new HistorialClinicoPaciente();
			Pacientes pacienteExample = new Pacientes();
			Pacientes pacienteVacio = new Pacientes();
			List<Pacientes>  pacienteList = new ArrayList<Pacientes>();			
			
			pacienteExample.setHistorialClinico(historialClin);
			busquedaHp.setHistorialClinico(historialClin);
			
			pacienteList = pacientesService.buscarNoPaginable(null, null, pacienteExample, null);
			if( !pacienteList.isEmpty() ) {
				busquedaHp.setPaciente(pacienteList.get(0));
			}else {
				pacienteVacio.setPersonas(new Personas());
				busquedaHp.setPaciente(pacienteVacio);
			}
			
			busquedaList.add(busquedaHp);
		}
						
        return new ResponseEntity<List<HistorialClinicoPaciente>>(busquedaList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody HistorialClinicoPaciente historialClinicoPaciente, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();		
		HistorialClinico historialClinicoNew = null;
		HistorialClinico historialClinicoExistente = null;
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( historialClinicoPaciente.getPaciente() == null || historialClinicoPaciente.getPaciente().getPacienteId() == null ) {
			response.put("mensaje", "Error: Datos del paciente es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
		
		Pacientes paciente = pacientesService.findById(historialClinicoPaciente.getPaciente().getPacienteId());
		
		if ( paciente == null ) {
			response.put("mensaje", "Error: No se encontro paciente con id " + historialClinicoPaciente.getPaciente().getPacienteId() );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
		if ( paciente.getHistorialClinico() != null && paciente.getHistorialClinico().getHistorialClinicoId() != null ) {
			response.put("mensaje", "Error: El paciente con id " + paciente.getPacienteId() 
			+ " ya cuenta con historial clinico con id " + paciente.getHistorialClinico().getHistorialClinicoId() );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
				
		try {
			historialClinicoPaciente.setPaciente(paciente);
			historialClinicoNew = historialesClinicosService.guardar(historialClinicoPaciente);
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
