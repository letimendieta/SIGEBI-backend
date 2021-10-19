package com.sigebi.service;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import com.lowagie.text.DocumentException;
import com.sigebi.util.exceptions.SigebiException;

public interface ReportService {
	
    String exportReport(String reportFormat, Integer consultaid) throws SigebiException, SQLException, FileNotFoundException, JRException;
    
    String generarSegundaHoja(String reportFormat, Integer anho, Integer mes) throws SigebiException, SQLException ;
    
    String generarTerceraHoja(String reportFormat, Integer anho, Integer mes) throws SigebiException, SQLException;
    
    String unionEstamentos(String reportFormat, HashMap<String, Object> filtros) throws FileNotFoundException, JRException, SQLException, SigebiException;
    
    String informeMensual(String reportFormat, HashMap<String, Object> filtros) throws FileNotFoundException, JRException, SQLException, SigebiException;
    
    String generarInformeMensualAtencionMedica(String reportFormat, Integer anho, Integer mes) throws SigebiException, SQLException ;
    
    String generarInformeMensualEnfermeria(String reportFormat, Integer anho, Integer mes) throws SigebiException, SQLException ;
    
    String generarInformeMensualElectrocardiograma(String reportFormat, Integer anho, Integer mes) throws SigebiException, SQLException ;

    void concatenarPDF() throws SigebiException, IOException, DocumentException;
    
    void concatenarInformeMensual() throws SigebiException, IOException, DocumentException;
}
