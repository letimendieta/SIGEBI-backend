package com.sigebi.service.impl;

import com.sigebi.clases.UnionEstamentos;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Parametros;
import com.sigebi.service.ParametrosService;
import com.sigebi.service.ReportService;
import com.sigebi.util.ConcatenarPDF;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private IPersonasDao iPersonasDao;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ParametrosService parametrosService;


    public String exportReport(String reportFormat, Integer consultaid) throws Exception {
        Parametros pathParametroReportes = parametrosService.findByCodigo("PATH_REPORTE");
        String path = pathParametroReportes.getValor();

        File file = ResourceUtils.getFile("classpath:reportes/receta.jrxml");

        Connection conn = dataSource.getConnection();
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        InputStream in = getClass().getResourceAsStream("/reportes/receta.jrxml");

        JasperCompileManager.compileReport(in);
        JRSaver.saveObject(jasperReport, "src/main/resources/reportes/receta.jasper");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("consultaid",consultaid);
        parameters.put("consulta_id",consultaid);
        parameters.put("subReporteDir", "src/main/resources/reportes/" );
        parameters.put("reportLogo", "src/main/resources/reportes/" );
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint,   path+ "receta.html");
            JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            jasperViewer.setVisible(true);
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "receta.pdf");
            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
            jasperViewer.setVisible(true);

        }

        conn.close();//para probar

        return "report generated in path : " + path ;
    }
    public String unionEstamentos(String reportFormat, HashMap<String, Object>  filtros) throws FileNotFoundException, JRException, SQLException {
        Parametros pathParametroReportes = parametrosService.findByCodigo("PATH_REPORTE");
        String path = pathParametroReportes.getValor();

        File file = ResourceUtils.getFile("classpath:reportes/servicios_salud.jrxml");

        // Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sigebi", "postgres", "postgres");
        Connection conn = dataSource.getConnection();
        List<UnionEstamentos> listaReporte = new ArrayList<UnionEstamentos>();
        CallableStatement cstmt = conn.prepareCall("{call union_estamentos(?, ?)}");

        CallableStatement cstmtTotal = conn.prepareCall("{call suma_totalestamentos(?, ?)}");

        if (filtros.get("mes") != null) {
            cstmt.setInt(1, Integer.parseInt(filtros.get("mes").toString()));
            cstmtTotal.setInt(1, Integer.parseInt(filtros.get("mes").toString()));
        } else {
            cstmt.setNString(1, null);
            cstmtTotal.setNString(1, null);
        }

        if (filtros.get("anho") != null) {
            cstmt.setInt(2, Integer.parseInt(filtros.get("anho").toString()));
            cstmtTotal.setInt(2, Integer.parseInt(filtros.get("anho").toString()));
        } else {
            cstmt.setNString(2, null);
            cstmtTotal.setNString(2, null);
        }

        ResultSet resultUnion = cstmt.executeQuery();
        while (resultUnion.next()) {
            UnionEstamentos entity = new UnionEstamentos();
            entity.setCodcarrera(resultUnion.getString("codcarrera"));
            entity.setNombreestamento(resultUnion.getString("nombreestamento"));
            entity.setMasculino(resultUnion.getInt("masculino"));
            entity.setFemenino(resultUnion.getInt("femenino"));
            entity.setSuma(resultUnion.getInt("suma"));
            listaReporte.add(entity);
        }

        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listaReporte);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        InputStream in = getClass().getResourceAsStream("/reportes/servicios_salud.jrxml");

        JasperCompileManager.compileReport(in);
        JRSaver.saveObject(jasperReport, "src/main/resources/reportes/servicios_salud.jasper");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("anho",filtros.get("anho"));
        parameters.put("mes",filtros.get("mes"));
        parameters.put("itemsJRBean", itemsJRBean);
        parameters.put("subReporteDir", "src/main/resources/reportes/" );
        parameters.put("mesLetra", obtenerMes(Integer.parseInt(filtros.get("mes").toString())));

        Integer femenino =0;
        Integer masculino =0;
        Integer suma =0;
        ResultSet resultTotal = cstmtTotal.executeQuery();
        while (resultTotal.next()) {
            femenino = resultTotal.getInt("femenino");
            masculino = resultTotal.getInt("masculino");
            suma = resultTotal.getInt("suma");
        }

        parameters.put("totalFemenino", femenino);
        parameters.put("totalMasculino", masculino);
        parameters.put("totalSuma", suma);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, itemsJRBean);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "servicios_salud.html");
            //JasperViewer jasperViewer = new JasperViewer(jasperPrint);
            //jasperViewer.setVisible(true);
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "servicios_salud.pdf");
            JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
            //jasperViewer.setVisible(true);
        }
        return "report generated in path : " + path;
    }

    @Override
    public String generarSegundaHoja(String reportFormat, Integer anho, Integer mes) throws Exception {


        Connection conn = dataSource.getConnection();
        Parametros pathParametroReportes = parametrosService.findByCodigo("PATH_REPORTE");


        String pathReportes = pathParametroReportes.getValor();

        File file = ResourceUtils.getFile("classpath:reportes/reporte_atenciones.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        InputStream in = getClass().getResourceAsStream("/reportes/reporte_atenciones.jrxml");

        JasperCompileManager.compileReport(in);
        JRSaver.saveObject(jasperReport, "src/main/resources/reportes/reporte_atenciones.jasper");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("anho",anho);
        parameters.put("mes",mes);
        parameters.put("subReporteDir", "src/main/resources/reportes/" );
        parameters.put("reportLogo", "src/main/resources/reportes/" );


        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(pathReportes + "reporte_atenciones.pdf"));

        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig
                = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("fpuna");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
        conn.close();//para probar

        return "Reporte generado en : " + pathReportes;

    }

    @Override
    public String generarTerceraHoja(String reportFormat, Integer anho, Integer mes) throws Exception {


        Connection conn = dataSource.getConnection();
        Parametros pathParametroReportes = parametrosService.findByCodigo("PATH_REPORTE");

        System.out.println(" parametro " + pathParametroReportes.getValor());

        if (pathParametroReportes == null){
            throw new Exception("No existe la paramétrica con código 'PATH_REPORTES'");
        }


        String pathReportes = pathParametroReportes.getValor();

        File file = ResourceUtils.getFile("classpath:reportes/reporte_totales.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        InputStream in = getClass().getResourceAsStream("/reportes/reporte_totales.jrxml");

        JasperCompileManager.compileReport(in);
        JRSaver.saveObject(jasperReport, "src/main/resources/reportes/reporte_totales.jasper");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("anho",anho);
        parameters.put("mes",mes);
        parameters.put("subReporteDir", "src/main/resources/reportes/" );


        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(pathReportes + "reporte_totales.pdf"));

        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig
                = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("fpuna");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
        conn.close();//para probar

        return "Reporte generado en : " + pathReportes;

    }



    @Override
    public void concatenarPDF() throws Exception {
        Parametros pathParametroReportes = parametrosService.findByCodigo("PATH_REPORTE");

        System.out.println(" parametro " + pathParametroReportes.getValor());

        if (pathParametroReportes == null){
            throw new Exception("No existe la paramétrica con código 'PATH_REPORTES'");
        }
        String pathReportes = pathParametroReportes.getValor();

        ConcatenarPDF concatenarPDF = new ConcatenarPDF();
        concatenarPDF.copiar(pathReportes);
    }

    private String obtenerMes(int mes) {
        String respuesta = "";
        switch (mes) {
            case 1:
                respuesta = "ENERO";
                break;
            case 2:
                respuesta = "FEBRERO";
                break;
            case 3:
                respuesta = "MARZO";
                break;
            case 4:
                respuesta = "ABRIL";
                break;
            case 5:
                respuesta = "MAYO";
                break;
            case 6:
                respuesta = "JUNIO";
                break;
            case 7:
                respuesta = "JULIO";
                break;
            case 8:
                respuesta = "AGOSTO";
                break;
            case 9:
                respuesta = "SETIEMBRE";
                break;
            case 10:
                respuesta = "OCTUBRE";
                break;
            case 11:
                respuesta = "NOVIEMBRE";
                break;
            case 12:
                respuesta = "DICIEMBRE";
                break;
            default:
                return "";
        }
        return respuesta;
    }





}