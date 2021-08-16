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
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/funcionarios")
public class FuncionariosController {

	@Autowired
	private FuncionariosService funcionariosService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";
	
	public FuncionariosController(FuncionariosService funcionariosService) {
        this.funcionariosService = funcionariosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Funcionarios> funcionariosList = null;

		funcionariosList = funcionariosService.findAll();

		if( funcionariosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Funcionarios>>(funcionariosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Funcionarios funcionario = null;

		funcionario = funcionariosService.findById(id);
		
		if( funcionario == null ) {
			response.put("mensaje", "El funcionario con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Funcionarios>(funcionario, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarFuncionarios(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Funcionarios funcionario = null;
		if(!utiles.isNullOrBlank(filtros)) {
			funcionario = objectMapper.readValue(filtros, Funcionarios.class);
		}				
		
		List<Funcionarios> funcionariosList = new ArrayList<Funcionarios>();
		
		if ( funcionario == null ) {
			funcionario = new Funcionarios();
		}
		
		List<Personas> personasList = new ArrayList<Personas>();
		List<Integer> personasIds = new ArrayList<Integer>();
		if( funcionario.getPersonas() != null) {
			
			personasList = personasService.buscarNoPaginable(fromDate, toDate, funcionario.getPersonas());

			for( Personas persona : personasList ){
				personasIds.add(persona.getPersonaId());
			}
			if( personasList.isEmpty()) {
				return new ResponseEntity<List<Funcionarios>>(funcionariosList, HttpStatus.OK);
			}
		}
		
		if ("-1".equals(size)) {
			int total = funcionariosService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		funcionariosList = funcionariosService.buscar(fromDate, toDate, funcionario, personasIds, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<Funcionarios>>(funcionariosList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Funcionarios funcionario, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();		
		Funcionarios funcionarioNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( funcionario.getPersonas() == null ) {
			response.put("mensaje", "Error: Datos de la persona es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		funcionarioNew = funcionariosService.guardar(funcionario);
		
		response.put("mensaje", "El funcionario ha sido creada con éxito!");
		response.put("funcionario", funcionarioNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Funcionarios funcionario, BindingResult result) throws Exception {
		Map<String, Object> response = new HashMap<>();
		
		if ( funcionario.getFuncionarioId() == null ) {
			response.put("mensaje", "Error: funcionario id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Funcionarios funcionarioActual = funcionariosService.findById(funcionario.getFuncionarioId());
		Funcionarios funcionarioUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( funcionarioActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, la persona ID: "
					.concat(String.valueOf(funcionario.getFuncionarioId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		funcionarioUpdated = funcionariosService.actualizar(funcionario);

		response.put("mensaje", "El funcionario ha sido actualizado con éxito!");
		response.put("funcionario", funcionarioUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: funcionario id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Funcionarios funcionarioActual = funcionariosService.findById(id);
		
		if ( funcionarioActual == null ) {
			response.put("mensaje", "El funcionario ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		funcionariosService.delete(id);
		
		response.put("mensaje", "Funcionario eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
