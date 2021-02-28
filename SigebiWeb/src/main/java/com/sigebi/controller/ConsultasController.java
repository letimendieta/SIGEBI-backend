package com.sigebi.controller;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sigebi.clases.Reporte;
import com.sigebi.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigebi.dao.IConsultasDao;
import com.sigebi.entity.Anamnesis;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.entity.TerminoEstandar;
import com.sigebi.service.ConsultasService;
import com.sigebi.service.UtilesService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/consultas")
public class ConsultasController {

	@Autowired
	private IConsultasDao repo;
	@Autowired
	private ConsultasService consultasService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	private ReportService reportService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";

	@GetMapping
	public List<Consultas> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Consultas datoConsulta) {
		repo.save(datoConsulta);
	}

	@PutMapping
	public void modificar(@RequestBody Consultas datoConsulta) {
		repo.save(datoConsulta);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarConsultas(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Consultas consulta = null;
		if(!utiles.isNullOrBlank(filtros)) {
			consulta = objectMapper.readValue(filtros, Consultas.class);
		}				
		
		Map<String, Object> response = new HashMap<>();
		List<Consultas> consultasList = new ArrayList<Consultas>();
		
		if ( consulta == null ) {
			consulta = new Consultas();
		}
		if ( "-1".equals(size) ) {
			int total = consultasService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		try {
			consultasList = consultasService.buscar(fromDate, toDate, consulta, orderBy, orderDir, pageable);
			
			for(Consultas consultaResult : consultasList) {
				if( consultaResult.getDiagnosticos() != null
						&& consultaResult.getDiagnosticos().getTerminoEstandarPrincipal() == null ) {
					consultaResult.getDiagnosticos().setTerminoEstandarPrincipal(new TerminoEstandar());
				}
				if( consultaResult.getAnamnesis() == null ) {
					consultaResult.setAnamnesis(new Anamnesis());
				}
				if( consultaResult.getAnamnesis() != null
						&& consultaResult.getAnamnesis().getMotivoConsulta() == null ) {
					consultaResult.getAnamnesis().setMotivoConsulta(new MotivosConsulta());
				}
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
        return new ResponseEntity<List<Consultas>>(consultasList, HttpStatus.OK);
    }

	@PostMapping ("/reportes")
	@ResponseBody
	public ResponseEntity generateReport(@RequestBody Reporte reporte) throws FileNotFoundException, JRException, SQLException {

		reportService.exportReport(reporte.getFormat(),Integer.parseInt(reporte.getConsultaid()));

		return ResponseEntity.ok(HttpStatus.OK);
	}
}
