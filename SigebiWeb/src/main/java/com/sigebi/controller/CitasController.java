package com.sigebi.controller;

import java.time.LocalDate;
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
import com.sigebi.entity.Citas;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.service.CitasService;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/citas")
public class CitasController {

	@Autowired
	private CitasService citasService;
	@Autowired
	private FuncionariosService funcionariosService;
	@Autowired
	private PacientesService pacientesService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public CitasController(CitasService citasService) {
        this.citasService = citasService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Citas> citasList = null;
		
		citasList = citasService.findAll();
		
		if( citasList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Citas>>(citasList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Citas cita = null;
		
		cita = citasService.findById(id);
		
		if( cita == null ) {
			response.put("mensaje", "La cita con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Citas>(cita, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarCitas(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JSONObject jo = new JSONObject(filtros);
		String fechaString = jo.length()>0 && !jo.get("fecha").equals(null) ? (String) jo.get("fecha") : "";
		LocalDate fecha = null;
		
		//se quita fecha de filtros por que da error al mapear
		if( fechaString != null && !fechaString.equals("")) {
			String fechaAquitar = '"' + (String) jo.get("fecha") + '"';
			filtros = filtros.replace(fechaAquitar, "null");			
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			fecha = LocalDate.parse(fechaString, format);					
		}
		
		Citas cita = null;
		if(!utiles.isNullOrBlank(filtros)) {
			cita = objectMapper.readValue(filtros, Citas.class);
			cita.setFecha(fecha);
		}
		
		Map<String, Object> response = new HashMap<>();
		List<Citas> citasList = new ArrayList<Citas>();
		
		if ( cita == null ) {
			cita = new Citas();
		}
		
		List<Personas> personasList = new ArrayList<Personas>();
		List<Integer> personasId = new ArrayList<Integer>();
		
		List<Funcionarios> funcionariosList = new ArrayList<Funcionarios>();		
		List<Integer> funcionariosIds = new ArrayList<Integer>();		
		if( cita.getFuncionarios() != null) {
			
				if(cita.getFuncionarios().getPersonas() != null) {
					personasList = personasService.buscar(null, null, cita.getFuncionarios().getPersonas(), PageRequest.of(0, 20));
					
					//Si se reciben datos de persona y si no se encuentra, retornar vacio
					if(personasList.isEmpty()) {
						return new ResponseEntity<List<Citas>>(citasList, HttpStatus.OK);
					}
					for( Personas persona : personasList ){
						personasId.add(persona.getPersonaId());
					}
				}				
				funcionariosList = funcionariosService.buscar(null, null, cita.getFuncionarios(), personasId, PageRequest.of(0, 20));
				
				//Si se reciben datos de funcionario y si no se encuentra, retornar vacio
				if(funcionariosList.isEmpty()) {
					return new ResponseEntity<List<Citas>>(citasList, HttpStatus.OK);
				}

			for( Funcionarios funcionario : funcionariosList ){
				funcionariosIds.add(funcionario.getFuncionarioId());
			}
		}
		
		List<Pacientes> pacientesList = new ArrayList<Pacientes>();
		List<Integer> pacientesIds = new ArrayList<Integer>();
		if( cita.getPacientes() != null) {
				personasId = new ArrayList<Integer>();
				personasList = new ArrayList<Personas>();
				
				if(cita.getPacientes().getPersonas() != null) {
					personasList = personasService.buscar(null, null, cita.getPacientes().getPersonas(), PageRequest.of(0, 20));
					
					//Si se reciben datos de persona y si no se encuentra, retornar vacio
					if(personasList.isEmpty()) {
						return new ResponseEntity<List<Citas>>(citasList, HttpStatus.OK);
					}
					for( Personas persona : personasList ){
						personasId.add(persona.getPersonaId());
					}
				}
				pacientesList = pacientesService.buscar(null, null, cita.getPacientes(), personasId, PageRequest.of(0, 20));
				
				//Si se reciben datos de paciente y si no se encuentra, retornar vacio
				if(pacientesList.isEmpty()) {
					return new ResponseEntity<List<Citas>>(citasList, HttpStatus.OK);
				}
			for( Pacientes paciente : pacientesList ){
				pacientesIds.add(paciente.getPacienteId());
			}
		}
		
		citasList = citasService.buscar(fromDate, toDate, cita, funcionariosIds, pacientesIds, pageable);
		
        return new ResponseEntity<List<Citas>>(citasList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Citas cita, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();		
		Citas citaNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		citaNew = citasService.guardar(cita);

		response.put("mensaje", "La cita ha sido creado con éxito!");
		response.put("cita", citaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Citas cita, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( cita.getCitaId() == null ) {
			response.put("mensaje", "Error: cita id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Citas citaActual = citasService.findById(cita.getCitaId());
		Citas citaUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( citaActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, la cita ID: "
					.concat(String.valueOf(cita.getCitaId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		citaUpdated = citasService.actualizar(cita);;

		response.put("mensaje", "La cita ha sido actualizada con éxito!");
		response.put("cita", citaUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: cita id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Citas citaActual = citasService.findById(id);
		
		if ( citaActual == null ) {
			response.put("mensaje", "La cita ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}		
		
		citasService.delete(id);

		response.put("mensaje", "Paciente eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
