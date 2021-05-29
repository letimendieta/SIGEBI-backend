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
import com.sigebi.entity.Medicamentos;
import com.sigebi.entity.Stock;
import com.sigebi.service.InsumosMedicosService;
import com.sigebi.service.MedicamentosService;
import com.sigebi.service.StockService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/stock")
public class StockController {

	@Autowired
	private StockService stockService;
	@Autowired
	private InsumosMedicosService insumosMedicosService;
	@Autowired
	private MedicamentosService medicamentosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";	
		
	public StockController(StockService stockService) {
        this.stockService = stockService;
    }

	@GetMapping
	public ResponseEntity<?> listar() {
		Map<String, Object> response = new HashMap<>();
		List<Stock> stockList = null;

		stockList = stockService.findAll();

		if( stockList.isEmpty()) {
			response.put("mensaje", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Stock>>(stockList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id) throws SigebiException{
		Stock stock = null;

		stock = stockService.obtener(id);
		
		return new ResponseEntity<Stock>(stock, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarStock(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Stock stock = null;
		if(!utiles.isNullOrBlank(filtros)) {
			stock = objectMapper.readValue(filtros, Stock.class);
		}				
		
		List<Stock> stockList = new ArrayList<Stock>();
		
		if ( stock == null ) {
			stock = new Stock();
		}
		
		List<Integer> insumosIds = new ArrayList<Integer>();
		if( stock.getInsumosMedicos() != null) {
			List<InsumosMedicos> insumosList = new ArrayList<InsumosMedicos>();			
			
			insumosList = insumosMedicosService.buscar(fromDate, toDate, stock.getInsumosMedicos(), pageable);

			for( InsumosMedicos insumo : insumosList ){
				insumosIds.add(insumo.getInsumoMedicoId());
			}
			if( insumosList.isEmpty()) {
				return new ResponseEntity<List<InsumosMedicos>>(insumosList, HttpStatus.OK);
			}
		}
			
		List<Integer> medicamentosIds = new ArrayList<Integer>();
		if( stock.getMedicamentos() != null) {
			List<Medicamentos> medicamentosList = new ArrayList<Medicamentos>();			
			
			medicamentosList = medicamentosService.buscar(fromDate, toDate, stock.getMedicamentos(), pageable);

			for( Medicamentos medicamento : medicamentosList ){
				medicamentosIds.add(medicamento.getMedicamentoId());
			}
			if( medicamentosList.isEmpty()) {
				return new ResponseEntity<List<Medicamentos>>(medicamentosList, HttpStatus.OK);
			}
		}
		
		stockList = stockService.buscar(fromDate, toDate, stock, insumosIds, medicamentosIds, pageable);
		
        return new ResponseEntity<List<Stock>>(stockList, HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody Stock stock, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();		
		Stock stockNew = null;
		
		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		stockNew = stockService.save(stock);
		
		response.put("mensaje", "El stock ha sido creada con éxito!");
		response.put("stock", stockNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> modificar(@Valid @RequestBody Stock stock, BindingResult result) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
		
		if ( stock.getStockId() == null ) {
			response.put("mensaje", "Error: stock id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Stock stockActual = stockService.obtener(stock.getStockId());
		Stock stockUpdated = null;

		if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if ( stockActual == null ) {
			response.put("mensaje", "Error: no se pudo editar, el stock ID: "
					.concat(String.valueOf(stock.getStockId()).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		stockUpdated = stockService.actualizar(stock);

		response.put("mensaje", "El stock ha sido actualizada con éxito!");
		response.put("stock", stockUpdated);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) throws SigebiException {
		Map<String, Object> response = new HashMap<>();
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			response.put("mensaje", "Error: stock id es requerido");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		Stock stockActual = stockService.obtener(id);
		
		if ( stockActual == null ) {
			response.put("mensaje", "La stock ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}		

		stockService.delete(id);
		
		response.put("mensaje", "Stock eliminado con éxito!");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
