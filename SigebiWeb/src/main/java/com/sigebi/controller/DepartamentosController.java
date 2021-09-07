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
import com.sigebi.entity.Departamentos;
import com.sigebi.security.service.RolService;
import com.sigebi.service.DepartamentosService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Globales;
import com.sigebi.util.Mensaje;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/departamentos")
public class DepartamentosController {

	@Autowired
	private DepartamentosService departamentosService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	private RolService rolService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public DepartamentosController(DepartamentosService departamentosService) {
        this.departamentosService = departamentosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Departamentos> departamentosList = null;
		
		departamentosList = departamentosService.findAll();

		if( departamentosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Departamentos>>(departamentosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Departamentos departamento = null;

		departamento = departamentosService.findById(id);
		
		if( departamento == null ) {
			response.put("mensaje", "El departamento con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Departamentos>(departamento, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarDepartamentos(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Departamentos departamento = null;
		if(!utiles.isNullOrBlank(filtros)) {
			departamento = objectMapper.readValue(filtros, Departamentos.class);
		}				
		
		List<Departamentos> departamentosList = new ArrayList<Departamentos>();
		
		if ( departamento == null ) {
			departamento = new Departamentos();
		}
		
		if ("-1".equals(size)) {
			int total = departamentosService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		departamentosList = departamentosService.buscar(fromDate, toDate, departamento, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<Departamentos>>(departamentosList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Departamentos departamento, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		Departamentos departamentoNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		departamentoNew = departamentosService.save(departamento);
		
		response.put("mensaje", "El departamento ha sido creado con éxito!");
		response.put("departamento", departamentoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Departamentos departamento, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		
		if ( departamento.getDepartamentoId() == null ) {
			response.put("mensaje", "Error: departamento id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Departamentos departamentoActual = departamentosService.findById(departamento.getDepartamentoId());
		Departamentos departamentoUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( departamentoActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el departamento ID: "
					.concat(String.valueOf(departamento.getDepartamentoId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		departamentoUpdated = departamentosService.save(departamento);;

		response.put("mensaje", "El departamento ha sido actualizado con éxito!");
		response.put("departamento", departamentoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if( !rolService.verificarRol(Globales.ROL_ABM_CONFIGURACION) ){
			return new ResponseEntity(new Mensaje("No cuenta con el rol requerido "), HttpStatus.UNAUTHORIZED);
		}
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: departamento id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Departamentos departamentoActual = departamentosService.findById(id);
		
		if ( departamentoActual == null ) {
			response.put("mensaje", "La departamento ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		departamentosService.delete(id);
		
		response.put("mensaje", "Departamento eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
