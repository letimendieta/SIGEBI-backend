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
import com.sigebi.entity.InsumosMedicos;
import com.sigebi.service.InsumosMedicosService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/insumos-medicos")
public class InsumosMedicosController {

	@Autowired
	private InsumosMedicosService insumosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public InsumosMedicosController(InsumosMedicosService insumosService) {
        this.insumosService = insumosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<InsumosMedicos> insumosList = null;

		insumosList = insumosService.listar();

		if( insumosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<InsumosMedicos>>(insumosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		InsumosMedicos insumo = null;

		insumo = insumosService.obtener(id);
		
		if( insumo == null ) {
			response.put("mensaje", "El insumo con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<InsumosMedicos>(insumo, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarInsumoMedico(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
				
		InsumosMedicos insumo = null;
		if(!utiles.isNullOrBlank(filtros)) {
			insumo = objectMapper.readValue(filtros, InsumosMedicos.class);
		}				
		
		List<InsumosMedicos> insumosList = new ArrayList<InsumosMedicos>();
		
		if ( insumo == null ) {
			insumo = new InsumosMedicos();
		}

		insumosList = insumosService.buscar(fromDate, toDate, insumo, pageable);
	
        return new ResponseEntity<List<InsumosMedicos>>(insumosList, HttpStatus.OK);
    }
	
	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody InsumosMedicos insumoMedico, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		InsumosMedicos insumosMedicoNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		insumosMedicoNew = insumosService.guardar(insumoMedico);
		
		response.put("mensaje", "El insumo medico ha sido creado con éxito!");
		response.put("insumoMedico", insumosMedicoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody InsumosMedicos insumoMedico, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
		
		InsumosMedicos insumoMedicoUpdated = null;		

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		insumoMedicoUpdated = insumosService.actualizar(insumoMedico);

		response.put("mensaje", "El insumo medico ha sido actualizado con éxito!");
		response.put("insumo", insumoMedicoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: insumo id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		InsumosMedicos insumoActual = insumosService.obtener(id);
		
		if ( insumoActual == null ) {
			response.put("mensaje", "El insumo ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		insumosService.eliminar(id);
		
		response.put("mensaje", "Insumo eliminada con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
