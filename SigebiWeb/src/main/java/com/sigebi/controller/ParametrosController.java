package com.sigebi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.sigebi.dao.IParametrosDao;
import com.sigebi.entity.Parametros;
import com.sigebi.service.ParametrosService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/parametros")
public class ParametrosController {
	
	@Autowired
	private ParametrosService parametrosService;
	@Autowired
	private UtilesService utiles;
	private static final String DATE_PATTERN = "yyyy/MM/dd";
	
	@Autowired
	private IParametrosDao repo;
	
	public ParametrosController(ParametrosService parametrosService) {
        this.parametrosService = parametrosService;
    }

	@GetMapping
	public List<Parametros> listar() {
		return repo.findAll();
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarPersonas(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Parametros parametro = null;
		if(!utiles.isNullOrBlank(filtros)) {
			parametro = objectMapper.readValue(filtros, Parametros.class);
		}				
		
		Map<String, Object> response = new HashMap<>();
		List<Parametros> parametroList = new ArrayList<Parametros>();
		
		if ( parametro == null ) {
			parametro = new Parametros();
		}
		try {
			parametroList = parametrosService.buscar(fromDate, toDate, parametro, orderBy, orderDir, pageable);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
        return new ResponseEntity<List<Parametros>>(parametroList, HttpStatus.OK);
    }

	@PostMapping
	public void insertar(@RequestBody Parametros parametro) {
		repo.save(parametro);
	}

	@PutMapping
	public void modificar(@RequestBody Parametros parametro) {
		repo.save(parametro);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
