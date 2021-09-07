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
import com.sigebi.clases.ProcesoPacienteHistorialClinico;
import com.sigebi.entity.Pacientes;
import com.sigebi.security.service.RolService;
import com.sigebi.service.FilesStorageService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Globales;
import com.sigebi.util.Mensaje;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/pacientes")
public class PacientesController {

	@Autowired
	private PacientesService pacientesService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	FilesStorageService storageService;
	@Autowired
	private RolService rolService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public PacientesController(PacientesService pacientesService) {
        this.pacientesService = pacientesService;
    }

	@GetMapping
	public ResponseEntity<?> listar() throws SigebiException{
		List<Pacientes> pacientesList = null;
		
		pacientesList = pacientesService.listar();
		
		return new ResponseEntity<List<Pacientes>>(pacientesList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id) throws SigebiException{
		Pacientes paciente = null;
		
		paciente = pacientesService.obtener(id);
		
		return new ResponseEntity<Pacientes>(paciente, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarPacientes(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException, DataAccessException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<Pacientes> pacientesList = new ArrayList<Pacientes>();
		
		Pacientes paciente = null;
		if(!utiles.isNullOrBlank(filtros)) {
			paciente = objectMapper.readValue(filtros, Pacientes.class);
		}	
		
		if ( paciente == null ) {
			paciente = new Pacientes();
		}
		
		if ("-1".equals(size)) {
			int total = pacientesService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		pacientesList = pacientesService.buscarPacientes(fromDate, toDate, paciente, orderBy, orderDir, pageable);
						
        return new ResponseEntity<List<Pacientes>>(pacientesList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> crear(@Valid @RequestBody Pacientes paciente, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();		
		Pacientes pacienteNew = null;
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());			
			throw new SigebiException.BusinessException(errors.toString());
		}		
		
		pacienteNew = pacientesService.guardar(paciente);		
						
		response.put("mensaje", "El paciente ha sido creado con éxito!");
		response.put("paciente", pacienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/paciente-historial-clinico")
	public ResponseEntity<?> insertarPacienteHistorialClinico(@Valid @RequestBody ProcesoPacienteHistorialClinico pacienteHistorialClinico, 
			BindingResult result) throws SigebiException{
				
		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			throw new SigebiException.BusinessException(errors.toString());
		}		
		
		Map<String, Object> response = new HashMap<>();		
		Pacientes pacienteNew = null;
		
		pacienteNew = pacientesService.guardarPacienteHistorialClinico(pacienteHistorialClinico);
					
		response.put("mensaje", "El paciente ha sido creado con éxito!");
		response.put("paciente", pacienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/paciente-historial-clinico")
	public ResponseEntity<?> actualizarPacienteHistorialClinico(@Valid @RequestBody ProcesoPacienteHistorialClinico pacienteHistorialClinico, BindingResult result)throws SigebiException {
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());			
			throw new SigebiException.BusinessException(errors.toString());
		}
		Pacientes pacienteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		pacienteNew = pacientesService.actualizarPacienteHistorialClinico(pacienteHistorialClinico);
					
		response.put("mensaje", "El paciente ha sido actualizado con éxito!");
		response.put("paciente", pacienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> actualizar(@Valid @RequestBody Pacientes paciente, BindingResult result) throws SigebiException {
		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			throw new SigebiException.BusinessException(errors.toString());
		}
		
		Map<String, Object> response = new HashMap<>();		
		Pacientes pacienteUpdated = null;
		
		pacienteUpdated = pacientesService.actualizar(paciente);;

		response.put("mensaje", "El paciente ha sido actualizado con éxito!");
		response.put("paciente", pacienteUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
		
		if( !rolService.verificarRol(Globales.ROL_ABM_PACIENTE) ){
			return new ResponseEntity(new Mensaje("No cuenta con el rol requerido "), HttpStatus.UNAUTHORIZED);
		}
									
		pacientesService.eliminar(id);
		
		response.put("mensaje", "Paciente eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
