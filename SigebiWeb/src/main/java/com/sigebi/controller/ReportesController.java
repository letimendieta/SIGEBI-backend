package com.sigebi.controller;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.sigebi.entity.Parametros;
import com.sigebi.security.service.UsuarioService;
import com.sigebi.service.ParametrosService;
import com.sigebi.service.ReportService;
import com.sigebi.util.exceptions.SigebiException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth/reportes")
public class ReportesController {

    @Autowired
    ReportService reportService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ParametrosService parametrosService;
    
    private static final String SEPARATOR = System.getProperty("file.separator");

    @PostMapping("/segundahoja")
    @ResponseBody
    public Map<String,Object> reporteSegundaHoja(@RequestBody String reportFormat, Integer anho, Integer mes) throws Exception {


        Map<String, Object> respuesta = new HashMap<>();
        String resultado = reportService.generarSegundaHoja(reportFormat,anho,mes);

        respuesta.put("Estado " ,ResponseEntity.ok(HttpStatus.OK));
        respuesta.put("resultado ", resultado);

        return  respuesta;

    }

    @PostMapping("/tercerahoja")
    @ResponseBody
    public Map<String,Object> reporteTerceraHoja(@RequestBody String reportFormat, Integer anho, Integer mes) throws Exception {


        Map<String, Object> respuesta = new HashMap<>();
        String resultado = reportService.generarTerceraHoja(reportFormat,anho,mes);

        respuesta.put("Estado " ,ResponseEntity.ok(HttpStatus.OK));
        respuesta.put("resultado ", resultado);

        return  respuesta;

    }

    @PostMapping("/union-estamentos")
    public ResponseEntity<InputStreamResource>  reporteProduccionEstadistica(
            @RequestParam(value = "anho") String anho,
            @RequestParam(value = "mes") String mes,
            @RequestParam(value = "formato") String formato)
            throws Exception {
        Parametros pathParametroReportes = parametrosService.findByCodigo("PATH_REPORTE");

        HashMap<String, Object> filtros = new HashMap<>();
        filtros.put("anho", Integer.parseInt(anho));
        filtros.put("mes", Integer.parseInt(mes));

        if (null == anho && mes == null) {
            throw new SigebiException("Debe enviar dato del mes y año");
        }
       String salida = reportService.unionEstamentos(formato, filtros);
       
       reportService.generarSegundaHoja(formato,Integer.parseInt(anho),Integer.parseInt(mes));
       reportService.generarTerceraHoja(formato,Integer.parseInt(anho),Integer.parseInt(mes));
       reportService.concatenarPDF();
       
	   String fileName = "reporte_general.pdf";
	   
	   File file = new File(pathParametroReportes.getValor()+SEPARATOR + fileName);
	   HttpHeaders headers = new HttpHeaders();
	   headers.add("content-disposition", "inline;filename=" +fileName);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }





   /* @GetMapping(value = "/ver")
    public ResponseEntity<InputStreamResource> getTermsConditions() throws FileNotFoundException {
        Parametros pathParametroReportes = parametrosService.findByCodigo("PATH_REPORTES");


        String fileName = "concatenatedPDF.pdf";
        File file = new File(pathParametroReportes.getValor()+fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "inline;filename=" +fileName);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }*/


}