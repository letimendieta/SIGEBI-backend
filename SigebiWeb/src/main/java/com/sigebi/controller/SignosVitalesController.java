package com.sigebi.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.json.JSONObject;
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
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.entity.SignosVitales;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.SignosVitalesService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/signos-vitales")
public class SignosVitalesController {

	@Autowired
	private SignosVitalesService signosVitalesService;
	@Autowired
	private FuncionariosService funcionariosService;
	@Autowired
	private PacientesService pacientesService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public SignosVitalesController(SignosVitalesService signosVitalesService) {
        this.signosVitalesService = signosVitalesService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<SignosVitales> signosVitalesList = null;
		try {
			signosVitalesList = signosVitalesService.findAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if( signosVitalesList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<SignosVitales>>(signosVitalesList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		SignosVitales signoVital = null;
		try {
			signoVital = signosVitalesService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if( signoVital == null ) {
			response.put("mensaje", "El signoVital con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<SignosVitales>(signoVital, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarSignosVitales(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		SignosVitales signoVital = null;
		if(!utiles.isNullOrBlank(filtros)) {
			signoVital = objectMapper.readValue(filtros, SignosVitales.class);
		}
		
		Map<String, Object> response = new HashMap<>();
		List<SignosVitales> signosVitalesList = new ArrayList<SignosVitales>();
		
		if ( signoVital == null ) {
			signoVital = new SignosVitales();
		}
		
		List<Personas> personasList = new ArrayList<Personas>();
		List<Integer> personasId = new ArrayList<Integer>();
		
		List<Funcionarios> funcionariosList = new ArrayList<Funcionarios>();		
		List<Integer> funcionariosIds = new ArrayList<Integer>();		
		if( signoVital.getFuncionarios() != null) {
			try {
				if(signoVital.getFuncionarios().getPersonas() != null) {
					personasList = personasService.buscar(null, null, signoVital.getFuncionarios().getPersonas(), PageRequest.of(0, 20));
					for( Personas persona : personasList ){
						personasId.add(persona.getPersonaId());
					}
				}				
				funcionariosList = funcionariosService.buscar(null, null, signoVital.getFuncionarios(), personasId, PageRequest.of(0, 20));
			} catch (DataAccessException  e) {
				response.put("mensaje", "Error al realizar la consulta de los datos del funcionario");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}  catch( Exception ex ){
				response.put("mensaje", "Ocurrio un error ");
				response.put("error", ex.getMessage());
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			for( Funcionarios funcionario : funcionariosList ){
				funcionariosIds.add(funcionario.getFuncionarioId());
			}
		}
		
		List<Pacientes> pacientesList = new ArrayList<Pacientes>();
		List<Integer> pacientesIds = new ArrayList<Integer>();
		if( signoVital.getPacientes() != null) {
			try {
				personasId = new ArrayList<Integer>();
				personasList = new ArrayList<Personas>();
				
				if(signoVital.getPacientes().getPersonas() != null) {
					personasList = personasService.buscar(null, null, signoVital.getPacientes().getPersonas(), PageRequest.of(0, 20));
					
					for( Personas persona : personasList ){
						personasId.add(persona.getPersonaId());
					}
				}
				pacientesList = pacientesService.buscar(null, null, signoVital.getPacientes(), personasId, PageRequest.of(0, 20));
									
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta de los datos del paciente");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			} catch( Exception ex ){
				response.put("mensaje", "Ocurrio un error ");
				response.put("error", ex.getMessage());
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			for( Pacientes paciente : pacientesList ){
				pacientesIds.add(paciente.getPacienteId());
			}
		}
		
		try {
			signosVitalesList = signosVitalesService.buscar(fromDate, toDate, signoVital, null, null, pageable);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta de los datos del signoVital");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
						
        return new ResponseEntity<List<SignosVitales>>(signosVitalesList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody SignosVitales procesoSignoVital, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		SignosVitales signoVitalNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
						
		try {
			signoVitalNew = signosVitalesService.save(procesoSignoVital);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al guardar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El signoVital ha sido creado con éxito!");
		response.put("signoVital", signoVitalNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody SignosVitales signoVital, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( signoVital.getSignoVitalId() == null ) {
			response.put("mensaje", "Error: signoVital id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		SignosVitales signoVitalActual = signosVitalesService.findById(signoVital.getSignoVitalId());
		SignosVitales signoVitalUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( signoVitalActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el signoVital ID: "
					.concat(String.valueOf(signoVital.getSignoVitalId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			signoVitalUpdated = signosVitalesService.save(signoVital);;

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el signoVital en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El signoVital ha sido actualizado con éxito!");
		response.put("signoVital", signoVitalUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: signoVital id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		SignosVitales signoVitalActual = signosVitalesService.findById(id);
		
		if ( signoVitalActual == null ) {
			response.put("mensaje", "El signoVital ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		try {
			signosVitalesService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el signoVital de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "SignoVital eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
