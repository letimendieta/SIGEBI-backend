package com.sigebi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.sigebi.clases.HistorialClinicoPaciente;
import com.sigebi.entity.Areas;
import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.service.FilesStorageService;
import com.sigebi.service.HistorialesClinicosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;

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
	
		historialClinicosList = historialesClinicosService.findAll();

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
		
		historialClinico = historialesClinicosService.findById(id);
		
		if( historialClinico == null ) {
			response.put("mensaje", "El historial clínico con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		if(historialClinico.getAreas() == null) {
			historialClinico.setAreas(new Areas());
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
		
		if(!utiles.isNullOrBlank(filtros)) {
			historialClinico = objectMapper.readValue(filtros, HistorialClinico.class);
		}
		
		List<HistorialClinico> historialClinicosList = new ArrayList<HistorialClinico>();
		
		if ( historialClinico == null ) {
			historialClinico = new HistorialClinico();
		}		
				
		historialClinicosList = historialesClinicosService.buscar(fromDate, toDate, historialClinico, pageable);
		
		for( HistorialClinico historialClin : historialClinicosList ){
			if( historialClin.getAreas() == null) {
				historialClin.setAreas(new Areas());
			}
		}
					
        return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
    }
	
	@GetMapping("/buscar/historialPaciente")
    public ResponseEntity<?> buscarHistorialClinicoPaciente(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException, SigebiException{		
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		HistorialClinicoPaciente busqueda = new HistorialClinicoPaciente();
		HistorialClinico historialClinico = null;
		Pacientes paciente = null;
		List<HistorialClinicoPaciente> busquedaList = new ArrayList<HistorialClinicoPaciente>();
		
		if(!utiles.isNullOrBlank(filtros)) {
			busqueda = objectMapper.readValue(filtros, HistorialClinicoPaciente.class);
		}
		historialClinico = busqueda.getHistorialClinico();
		paciente = busqueda.getPaciente();

		List<HistorialClinico> historialClinicosList = new ArrayList<HistorialClinico>();
		
		if ( historialClinico == null ) {
			historialClinico = new HistorialClinico();
		}
		
		Pacientes pacienteDb = new Pacientes();
		
		if( paciente != null && paciente.getPacienteId() != null) {
				
			pacienteDb = pacientesService.obtener(paciente.getPacienteId());
			
			//Si se reciben datos de paciente y si no se encuentra, retornar vacio
			if(pacienteDb == null) {
				return new ResponseEntity<List<HistorialClinico>>(historialClinicosList, HttpStatus.OK);
			}
		}
		
		historialClinicosList = historialesClinicosService.buscar(fromDate, toDate, historialClinico, pageable);
		
		for( HistorialClinico historialClin : historialClinicosList ){
			
				HistorialClinicoPaciente busquedaHp = new HistorialClinicoPaciente();
				Pacientes pacienteVacio = new Pacientes();
				List<Pacientes>  pacienteList = new ArrayList<Pacientes>();		
				
				busquedaHp.setHistorialClinico(historialClin);
				
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
		
		if ( historialClinico == null || historialClinico.getPacienteId() == null ) {
			response.put("mensaje", "Error: Datos del paciente es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
		
		Pacientes paciente = pacientesService.obtener(historialClinico.getPacienteId());
		
		if ( paciente == null ) {
			response.put("mensaje", "Error: No se encontró paciente con id " + historialClinico.getPacienteId() );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
		
		HistorialClinico historialClinicoPaciente = new HistorialClinico();
		
		List<HistorialClinico> historialClinicoDb = historialesClinicosService.buscarNoPaginable(null, null, historialClinicoPaciente); 
		
		if ( historialClinicoDb != null && !historialClinicoDb.isEmpty() ) {
			response.put("mensaje", "Error: El paciente con id " + paciente.getPacienteId() 
			+ " ya cuenta con historial clínico con id " + historialClinicoDb.get(0).getHistorialClinicoId() );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}		

		historialClinicoNew = historialesClinicosService.guardar(historialClinico);
		
		response.put("mensaje", "El historial clínico ha sido creado con éxito!");
		response.put("historialClinico", historialClinicoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody HistorialClinico historialClinico, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( historialClinico.getHistorialClinicoId() == null ) {
			response.put("mensaje", "Error: historial clínico id es requerido");
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
			response.put("mensaje", "Error: no se pudo editar, el historial clínico ID: "
					.concat(String.valueOf(historialClinico.getHistorialClinicoId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if ( historialClinico == null || historialClinico.getPacienteId() == null ) {
			response.put("mensaje", "Error: Datos del paciente es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
		
		Pacientes paciente = pacientesService.obtener(historialClinico.getPacienteId());
		
		if ( paciente == null ) {
			response.put("mensaje", "Error: No se encontró paciente con id " + historialClinico.getPacienteId() );
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.PRECONDITION_FAILED);
		}
				
		historialClinicoUpdated = historialesClinicosService.actualizar(historialClinico);;

		response.put("mensaje", "El historial clínico ha sido actualizado con éxito!");
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
			response.put("mensaje", "El historial clínico ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		historialesClinicosService.delete(id);
		
		response.put("mensaje", "Historial clínico eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
