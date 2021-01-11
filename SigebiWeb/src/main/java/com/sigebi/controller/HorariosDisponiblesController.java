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
import com.sigebi.entity.HorariosDisponibles;
import com.sigebi.entity.Personas;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.HorariosDisponiblesService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/horarios-disponibles")
public class HorariosDisponiblesController {

	@Autowired
	private HorariosDisponiblesService horariosDisponiblesService;
	@Autowired
	private FuncionariosService funcionariosService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public HorariosDisponiblesController(HorariosDisponiblesService horariosDisponiblesService) {
        this.horariosDisponiblesService = horariosDisponiblesService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<HorariosDisponibles> horariosDisponiblesList = null;
		try {
			horariosDisponiblesList = horariosDisponiblesService.findAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if( horariosDisponiblesList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<HorariosDisponibles>>(horariosDisponiblesList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		HorariosDisponibles horariosDisponible = null;
		try {
			horariosDisponible = horariosDisponiblesService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if( horariosDisponible == null ) {
			response.put("mensaje", "El horariosDisponible con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<HorariosDisponibles>(horariosDisponible, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarHorariosDisponibles(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		JSONObject jo = new JSONObject(filtros);
		String fechaString = jo.length()>0 && !jo.get("fecha").equals(null) ? (String) jo.get("fecha") : "";
		LocalDate fecha = null;
		
		//se quita fecha de filtros por que da error al mapear
		if(!fechaString.equals(null) && !fechaString.equals("")) {
			String fechaAquitar = '"' + (String) jo.get("fecha") + '"';
			filtros = filtros.replace(fechaAquitar, "null");			
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			fecha = LocalDate.parse(fechaString, format);					
		}
		
		HorariosDisponibles horariosDisponible = null;
		if(!utiles.isNullOrBlank(filtros)) {
			horariosDisponible = objectMapper.readValue(filtros, HorariosDisponibles.class);
			horariosDisponible.setFecha(fecha);
		}
		
		Map<String, Object> response = new HashMap<>();
		List<HorariosDisponibles> horariosDisponiblesList = new ArrayList<HorariosDisponibles>();
		
		if ( horariosDisponible == null ) {
			horariosDisponible = new HorariosDisponibles();
		}
		
		List<Personas> personasList = new ArrayList<Personas>();
		List<Integer> personasId = new ArrayList<Integer>();
		
		List<Funcionarios> funcionariosList = new ArrayList<Funcionarios>();		
		List<Integer> funcionariosIds = new ArrayList<Integer>();		
		if( horariosDisponible.getFuncionarios() != null) {
			try {
				if(horariosDisponible.getFuncionarios().getPersonas() != null) {
					personasList = personasService.buscar(null, null, horariosDisponible.getFuncionarios().getPersonas(), PageRequest.of(0, 20));
					for( Personas persona : personasList ){
						personasId.add(persona.getPersonaId());
					}
				}				
				funcionariosList = funcionariosService.buscar(null, null, horariosDisponible.getFuncionarios(), personasId, PageRequest.of(0, 20));
			} catch (DataAccessException e) {
				response.put("mensaje", "Error al realizar la consulta de los datos del funcionario");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			for( Funcionarios funcionario : funcionariosList ){
				funcionariosIds.add(funcionario.getFuncionarioId());
			}
			if( funcionariosList.isEmpty()) {
				return new ResponseEntity<List<HorariosDisponibles>>(horariosDisponiblesList, HttpStatus.OK);
			}
		}		
		
		try {
			horariosDisponiblesList = horariosDisponiblesService.buscar(fromDate, toDate, horariosDisponible, 
																funcionariosIds, pageable);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta de los datos del horariosDisponible");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
						
        return new ResponseEntity<List<HorariosDisponibles>>(horariosDisponiblesList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody HorariosDisponibles horariosDisponible, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		HorariosDisponibles horariosDisponibleNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		try {
			horariosDisponibleNew = horariosDisponiblesService.guardar(horariosDisponible);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al guardar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El horario  ha sido creado con éxito!");
		response.put("horariosDisponible", horariosDisponibleNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody HorariosDisponibles horariosDisponible, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( horariosDisponible.getHorarioDisponibleId() == null ) {
			response.put("mensaje", "Error: horariosDisponible id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		HorariosDisponibles horariosDisponibleActual = horariosDisponiblesService.findById(horariosDisponible.getHorarioDisponibleId());
		HorariosDisponibles horariosDisponibleUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( horariosDisponibleActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el horario ID: "
					.concat(String.valueOf(horariosDisponible.getHorarioDisponibleId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {

			horariosDisponibleUpdated = horariosDisponiblesService.actualizar(horariosDisponible);;

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el horario en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El horario ha sido actualizado con éxito!");
		response.put("horariosDisponible", horariosDisponibleUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: horario id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		HorariosDisponibles horariosDisponibleActual = horariosDisponiblesService.findById(id);
		
		if ( horariosDisponibleActual == null ) {
			response.put("mensaje", "El horario ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		try {
			horariosDisponiblesService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el horario de la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Horario eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
