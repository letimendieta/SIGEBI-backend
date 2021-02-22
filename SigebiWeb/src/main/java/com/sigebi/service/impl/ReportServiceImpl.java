package com.sigebi.service.impl;

import com.sigebi.dao.IPersonasDao;
import com.sigebi.service.ReportService;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private IPersonasDao iPersonasDao;



    public String exportReport(String reportFormat, Integer consultaid) throws FileNotFoundException, JRException, SQLException {
        String path = "/home/leticia/JaspersoftWorkspace/MyReports";
        //List<Personas> personas = iPersonasDao.findByCedula("5432144");
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:receta.jrxml");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sigebi", "postgres", "postgres");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JasperCompileManager.compileReportToFile(path + "/tratamiento.jrxml", "tratamiento.jasper");
        //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(personas);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("consultaid",consultaid);
        parameters.put("consulta_id",consultaid);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "/receta.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "/receta.pdf");
        }

        return "report generated in path : " + path;
    }
}
