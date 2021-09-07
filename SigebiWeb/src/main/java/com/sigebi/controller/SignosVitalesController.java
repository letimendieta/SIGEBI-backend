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
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.entity.SignosVitales;
import com.sigebi.security.service.RolService;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.SignosVitalesService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Globales;
import com.sigebi.util.Mensaje;

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
	@Autowired
	private RolService rolService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public SignosVitalesController(SignosVitalesService signosVitalesService) {
        this.signosVitalesService = signosVitalesService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<SignosVitales> signosVitalesList = null;

		signosVitalesList = signosVitalesService.findAll();

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

		signoVital = signosVitalesService.findById(id);
		
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
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		SignosVitales signoVital = null;
		if(!utiles.isNullOrBlank(filtros)) {
			signoVital = objectMapper.readValue(filtros, SignosVitales.class);
		}
		
		List<SignosVitales> signosVitalesList = new ArrayList<SignosVitales>();
		
		if ( signoVital == null ) {
			signoVital = new SignosVitales();
		}
		
		List<Personas> personasList = new ArrayList<Personas>();
		List<Integer> personasId = new ArrayList<Integer>();
		
		List<Funcionarios> funcionariosList = new ArrayList<Funcionarios>();		
		List<Integer> funcionariosIds = new ArrayList<Integer>();		
		if( signoVital.getFuncionarios() != null) {
			if(signoVital.getFuncionarios().getPersonas() != null) {
				personasList = personasService.buscarNoPaginable(null, null, signoVital.getFuncionarios().getPersonas());
				for( Personas persona : personasList ){
					personasId.add(persona.getPersonaId());
				}
			}				
			funcionariosList = funcionariosService.buscarNoPaginable(null, null, signoVital.getFuncionarios(), personasId);

			for( Funcionarios funcionario : funcionariosList ){
				funcionariosIds.add(funcionario.getFuncionarioId());
			}
		}
		
		List<Pacientes> pacientesList = new ArrayList<Pacientes>();
		List<Integer> pacientesIds = new ArrayList<Integer>();
		if( signoVital.getPacientes() != null) {
			personasId = new ArrayList<Integer>();
			personasList = new ArrayList<Personas>();
			
			if(signoVital.getPacientes().getPersonas() != null) {
				personasList = personasService.buscarNoPaginable(null, null, signoVital.getPacientes().getPersonas());
				
				for( Personas persona : personasList ){
					personasId.add(persona.getPersonaId());
				}
			}
			pacientesList = pacientesService.buscarNoPaginable(null, null, signoVital.getPacientes(), personasId);				

			for( Pacientes paciente : pacientesList ){
				pacientesIds.add(paciente.getPacienteId());
			}
		}
		
		int total = signosVitalesService.count();
		
		if( total == 0) {
			return new ResponseEntity<List<SignosVitales>>(signosVitalesList, HttpStatus.OK);
		}
		
		if ("-1".equals(size)) {			
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		signosVitalesList = signosVitalesService.buscar(fromDate, toDate, signoVital, orderBy, orderDir, pageable);
						
        return new ResponseEntity<List<SignosVitales>>(signosVitalesList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody SignosVitales procesoSignoVital, BindingResult result) throws Exception {
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
						
		signoVitalNew = signosVitalesService.save(procesoSignoVital);
		
		response.put("mensaje", "El signo vital ha sido creado con éxito!");
		response.put("signoVital", signoVitalNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody SignosVitales signoVital, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( signoVital.getSignoVitalId() == null ) {
			response.put("mensaje", "Error: signo vital id es requerido");
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
			response.put("mensaje", "Error: no se pudo editar, el signo vital ID: "
					.concat(String.valueOf(signoVital.getSignoVitalId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		signoVitalUpdated = signosVitalesService.save(signoVital);;

		response.put("mensaje", "El signo vital ha sido actualizado con éxito!");
		response.put("signoVital", signoVitalUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if( !rolService.verificarRol(Globales.ROL_ABM_PACIENTE) ){
			return new ResponseEntity(new Mensaje("No cuenta con el rol requerido "), HttpStatus.UNAUTHORIZED);
		}
		
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

		signosVitalesService.delete(id);
		
		response.put("mensaje", "Signo vital eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
