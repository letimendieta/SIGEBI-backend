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
import com.sigebi.entity.Medicamentos;
import com.sigebi.service.MedicamentosService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/medicamentos")
public class MedicamentosController {

	@Autowired
	private MedicamentosService medicamentosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public MedicamentosController(MedicamentosService medicamentosService) {
        this.medicamentosService = medicamentosService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Medicamentos> medicamentosList = null;

		medicamentosList = medicamentosService.listar();

		if( medicamentosList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Medicamentos>>(medicamentosList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Medicamentos medicamento = null;

		medicamento = medicamentosService.obtener(id);
		
		if( medicamento == null ) {
			response.put("mensaje", "El medicamento con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Medicamentos>(medicamento, HttpStatus.OK);
	}
	
		
	@GetMapping("/buscar")
    public ResponseEntity<?> buscar(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
				
		Medicamentos medicamento = null;
		if(!utiles.isNullOrBlank(filtros)) {
			medicamento = objectMapper.readValue(filtros, Medicamentos.class);
		}				
		
		List<Medicamentos> insumosList = new ArrayList<Medicamentos>();
		
		if ( medicamento == null ) {
			medicamento = new Medicamentos();
		}
		
		if ("-1".equals(size)) {
			int total = medicamentosService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		insumosList = medicamentosService.buscar(fromDate, toDate, medicamento, orderBy, orderDir, pageable);
	
        return new ResponseEntity<List<Medicamentos>>(insumosList, HttpStatus.OK);
    }
	
	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Medicamentos medicamento, BindingResult result) {
		Map<String, Object> response = new HashMap<>();		
		Medicamentos medicamentoNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		medicamentoNew = medicamentosService.guardar(medicamento);
		
		response.put("mensaje", "El medicamento ha sido creado con éxito!");
		response.put("medicamento", medicamentoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Medicamentos medicamento, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
		
		Medicamentos medicamentoUpdated = null;		

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		medicamentoUpdated = medicamentosService.actualizar(medicamento);

		response.put("mensaje", "El medicamento ha sido actualizado con éxito!");
		response.put("medicamento", medicamentoUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: medicamento id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Medicamentos medicamentoActual = medicamentosService.obtener(id);
		
		if ( medicamentoActual == null ) {
			response.put("mensaje", "El medicamento ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
					
		medicamentosService.eliminar(id);
		
		response.put("mensaje", "Medicamento eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
