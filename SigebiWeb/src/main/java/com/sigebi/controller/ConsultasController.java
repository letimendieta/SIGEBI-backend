package com.sigebi.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigebi.clases.ConsultasResult;
import com.sigebi.clases.DiagnosticosResult;
import com.sigebi.clases.Reporte;
import com.sigebi.dao.IConsultasDao;
import com.sigebi.entity.Anamnesis;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.entity.Personas;
import com.sigebi.service.ConsultasService;
import com.sigebi.service.EnfermedadesCie10Service;
import com.sigebi.service.ReportService;
import com.sigebi.service.UtilesService;

import net.sf.jasperreports.engine.JRException;

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
	@Autowired
	private EnfermedadesCie10Service enfermedadesCie10Service;
	
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
		List<ConsultasResult> consultasListResult = new ArrayList<ConsultasResult>();
		
		ConsultasResult consultaResult = null;
		
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
			
			for(Consultas consultaBusqueda : consultasList) {
				consultaResult = new ConsultasResult();
				
				consultaResult.setConsultaId(consultaBusqueda.getConsultaId());
				consultaResult.setFecha(consultaBusqueda.getFecha());
				consultaResult.setPacienteId(consultaBusqueda.getPacienteId());
				consultaResult.setFechaCreacion(consultaBusqueda.getFechaCreacion());
				consultaResult.setUsuarioCreacion(consultaBusqueda.getUsuarioCreacion());
				consultaResult.setFechaModificacion(consultaBusqueda.getFechaModificacion());
				consultaResult.setUsuarioModificacion(consultaBusqueda.getUsuarioModificacion());
				consultaResult.setAreas(consultaBusqueda.getAreas());
				consultaResult.setTratamientos(consultaBusqueda.getTratamientos());
				consultaResult.setFuncionarios(consultaBusqueda.getFuncionarios());
				consultaResult.setAnamnesis(consultaBusqueda.getAnamnesis());
				
				if ( consultaBusqueda.getDiagnosticos() != null ) {
					DiagnosticosResult diagnosticoResult = new DiagnosticosResult();
					
					diagnosticoResult.setDiagnosticoId(consultaBusqueda.getDiagnosticos().getDiagnosticoId());	
					diagnosticoResult.setDiagnosticoPrincipal(consultaBusqueda.getDiagnosticos().getDiagnosticoPrincipal());
					diagnosticoResult.setDiagnosticoSecundario(consultaBusqueda.getDiagnosticos().getDiagnosticoSecundario());					
					diagnosticoResult.setFechaCreacion(consultaBusqueda.getDiagnosticos().getFechaCreacion());
					diagnosticoResult.setUsuarioCreacion(consultaBusqueda.getDiagnosticos().getUsuarioCreacion());
					diagnosticoResult.setFechaModificacion(consultaBusqueda.getDiagnosticos().getFechaModificacion());
					diagnosticoResult.setUsuarioModificacion(consultaBusqueda.getDiagnosticos().getUsuarioModificacion());
					
					consultaResult.setDiagnosticos(diagnosticoResult);
				}				
				
				if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10PrimariaId() == null ) {
					consultaResult.getDiagnosticos().setEnfermedadCie10Primaria(new EnfermedadesCie10());
				}else if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10PrimariaId() != null ) {
					EnfermedadesCie10 cie10 = enfermedadesCie10Service.findById(consultaBusqueda.getDiagnosticos().getEnfermedadCie10PrimariaId());
					
					consultaResult.getDiagnosticos().setEnfermedadCie10Primaria(cie10);
				}
				
				if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10SecundariaId() == null ) {
					consultaResult.getDiagnosticos().setEnfermedadCie10Secundaria(new EnfermedadesCie10());
				}else if( consultaBusqueda.getDiagnosticos() != null
						&& consultaBusqueda.getDiagnosticos().getEnfermedadCie10SecundariaId() != null ) {
					EnfermedadesCie10 cie10 = enfermedadesCie10Service.findById(consultaBusqueda.getDiagnosticos().getEnfermedadCie10SecundariaId());
					
					consultaResult.getDiagnosticos().setEnfermedadCie10Secundaria(cie10);
				}
				
				if( consultaResult.getAnamnesis() == null ) {
					consultaResult.setAnamnesis(new Anamnesis());
				}
				if( consultaResult.getAnamnesis() != null
						&& consultaResult.getAnamnesis().getMotivoConsulta() == null ) {
					consultaResult.getAnamnesis().setMotivoConsulta(new MotivosConsulta());
				}
				
				if( consultaResult.getFuncionarios() == null ) {
					consultaResult.setFuncionarios(new Funcionarios());
					consultaResult.getFuncionarios().setPersonas(new Personas());
				}
				
				consultasListResult.add(consultaResult);
			}
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch( Exception ex ){
			response.put("mensaje", "Ocurrio un error ");
			response.put("error", ex.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
        return new ResponseEntity<List<ConsultasResult>>(consultasListResult, HttpStatus.OK);
    }

	@PostMapping ("/reportes")
	@ResponseBody
	public ResponseEntity generateReport(@RequestBody Reporte reporte) throws IOException, JRException, SQLException {


		String resultado = reportService.exportReport(reporte.getFormat(),Integer.parseInt(reporte.getConsultaid()));

		return ResponseEntity.ok(HttpStatus.OK);
	}
}
