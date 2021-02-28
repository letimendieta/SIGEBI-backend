package com.sigebi.service;

import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public interface ReportService {
    public String exportReport(String reportFormat, Integer consultaid) throws FileNotFoundException, JRException, SQLException;
}
