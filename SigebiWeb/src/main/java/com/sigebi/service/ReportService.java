package com.sigebi.service;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;

public interface ReportService {
    String exportReport(String reportFormat, Integer consultaid) throws Exception;


    String generarSegundaHoja(String reportFormat, Integer anho, Integer mes) throws Exception;

    String generarTerceraHoja(String reportFormat, Integer anho, Integer mes) throws Exception;
    String unionEstamentos(String reportFormat, HashMap<String, Object> filtros) throws FileNotFoundException, JRException, SQLException;

    void concatenarPDF() throws Exception;
}
