package com.sigebi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigebi.clases.ProcesoDiagnosticoTratamiento;
import com.sigebi.entity.Consultas;
import com.sigebi.service.ProcesoDiagnosticoTratamientoService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/proceso-diagnostico-tratamiento")
public class ProcesoDiagnosticoTratamientoController {

	@Autowired
	private ProcesoDiagnosticoTratamientoService procesoDiagnosticoTratamientoService;
		
	public ProcesoDiagnosticoTratamientoController(ProcesoDiagnosticoTratamientoService procesoDiagnosticoTratamientoService) {
        this.procesoDiagnosticoTratamientoService = procesoDiagnosticoTratamientoService;
    }
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody ProcesoDiagnosticoTratamiento procesoDiagnosticoTratamiento,
			BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();		
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		Consultas consulta;
		
		consulta = procesoDiagnosticoTratamientoService.guardar(procesoDiagnosticoTratamiento);	
		
		response.put("mensaje", "La consulta ha sido creada con éxito!");
		response.put("consulta", consulta);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
}
