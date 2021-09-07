package com.sigebi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigebi.clases.ConsultasResult;
import com.sigebi.clases.Reporte;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.Parametros;
import com.sigebi.service.ConsultasService;
import com.sigebi.service.ParametrosService;
import com.sigebi.service.ReportService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;

import net.sf.jasperreports.engine.JRException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/consultas")
public class ConsultasController {

	@Autowired
	private ConsultasService consultasService;
	@Autowired
	private UtilesService utiles;
	@Autowired
	private ReportService reportService;
	@Autowired
    ParametrosService parametrosService;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";
	private static final String SEPARATOR = System.getProperty("file.separator");

	@GetMapping
	public List<Consultas> listar() throws SigebiException {
		List<Consultas> consultas = consultasService.listar();
		return consultas;
	}

	@PostMapping
	public void crear(@RequestBody Consultas datoConsulta, BindingResult result) throws SigebiException {
		
		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());			
			throw new SigebiException.BusinessException(errors.toString());
		}
		
		consultasService.guardar(datoConsulta);
	}

	@PutMapping
	public void actualizar(@RequestBody Consultas datoConsulta, BindingResult result) throws SigebiException {
		
		if( result.hasErrors() ) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());			
			throw new SigebiException.BusinessException(errors.toString());
		}
		
		consultasService.actualizar(datoConsulta);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) throws SigebiException {
		consultasService.eliminar(id);
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
            Pageable pageable) throws JsonMappingException, JsonProcessingException, DataAccessException, SigebiException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Consultas consulta = null;
		if(!utiles.isNullOrBlank(filtros)) {
			consulta = objectMapper.readValue(filtros, Consultas.class);
		}				
		
		List<ConsultasResult> consultasListResult = new ArrayList<ConsultasResult>();
				
		if ( consulta == null ) {
			consulta = new Consultas();
		}
		if ( "-1".equals(size) ) {
			int total = consultasService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}			
		
		consultasListResult = consultasService.buscarConsultas(fromDate, toDate, consulta, orderBy, orderDir, pageable);
		
        return new ResponseEntity<List<ConsultasResult>>(consultasListResult, HttpStatus.OK);
    }

	@PostMapping ("/reportes")
	@ResponseBody
	public ResponseEntity generateReport(@RequestBody Reporte reporte) throws NumberFormatException, Exception {


		String resultado = reportService.exportReport(reporte.getFormat(),Integer.parseInt(reporte.getConsultaid()));

		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PostMapping ("/receta")
	@ResponseBody
	public ResponseEntity<InputStreamResource> generarReceta(@RequestBody Reporte reporte) throws NumberFormatException, Exception {
		String pathReportes;

		String resultado = reportService.exportReport(reporte.getFormat(),Integer.parseInt(reporte.getConsultaid()));
		
		Parametros pathParametroReportes = parametrosService.findByCodigo(Globales.PATH_REPORTE);
		pathReportes = pathParametroReportes.getValor() + SEPARATOR;
		
		String fileName = "receta.pdf";
		   
	   File file = new File(pathReportes + fileName);
	   HttpHeaders headers = new HttpHeaders();
	   headers.add("content-disposition", "inline;filename=" + fileName);

	   InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
	
	   return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(file.length())
	            .contentType(MediaType.parseMediaType("application/pdf"))
	            .body(resource);
	}
	
	@GetMapping("/union-estamentos/{formato}")
	public ResponseEntity reporteProduccionEstadistica(
											@RequestParam(value = "anho") Integer anho,
											@RequestParam(value = "mes") Integer mes,
											@RequestParam(value = "formato") String formato)
			throws SQLException, IOException, JRException, SigebiException {
		HashMap<String, Object> filtros = new HashMap<>();
		filtros.put("anho", anho);
		filtros.put("mes", mes);
		
		if (null == anho && mes == null) {
			throw new SigebiException("Debe enviar dato del mes y el año");
		}
		String salida = reportService.unionEstamentos(formato, filtros);
		return ResponseEntity.ok(HttpStatus.OK);
	}
}
