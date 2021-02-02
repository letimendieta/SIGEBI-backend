package com.sigebi.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IConsultasDao;
import com.sigebi.dao.IDiagnosticosDao;
import com.sigebi.dao.IStockDao;
import com.sigebi.dao.ITratamientosDao;
import com.sigebi.dao.ITratamientosInsumosDao;
import com.sigebi.entity.Diagnosticos;
import com.sigebi.entity.Insumos;
import com.sigebi.entity.ProcesoDiagnosticoTratamiento;
import com.sigebi.entity.Stock;
import com.sigebi.entity.Tratamientos;
import com.sigebi.entity.TratamientosInsumos;
import com.sigebi.service.ProcesoDiagnosticoTratamientoService;


@Service
public class ProcesoDiagnosticoTratamientoServiceImpl implements ProcesoDiagnosticoTratamientoService{

	@Autowired
	private IDiagnosticosDao diagnosticoDao;	
	@Autowired
	private ITratamientosDao tratamientoDao;
	@Autowired
	private ITratamientosInsumosDao tratamientoInsumoDao;
	@Autowired
	private IConsultasDao consultaDao;
	

	@Override
	@Transactional
	public void save(ProcesoDiagnosticoTratamiento procesoDiagnosticoTratamiento) throws Exception {
		
		//Guardar diagnostico	
		Diagnosticos diagnostico = null;
		try {
			diagnostico = diagnosticoDao.save(procesoDiagnosticoTratamiento.getDiagnostico());
		} catch (Exception e) {
			throw new Exception("Error al guardar el diagnostico " + e.getMessage());
		}
		
		//Guardar el tratamiento
		Tratamientos tratamiento = null;
		try {
			tratamiento = tratamientoDao.save(procesoDiagnosticoTratamiento.getTratamiento());
		} catch (Exception e) {
			throw new Exception("Error al guardar el tratamiento " + e.getMessage());
		}
		
		//Guardar el tratamiento insumo
		try {
			for(TratamientosInsumos tratamientoInsumo : procesoDiagnosticoTratamiento.getTratamientoInsumoList()) {
				tratamientoInsumo.setTratamientos(tratamiento);
				tratamientoInsumoDao.save(tratamientoInsumo);				
			}			
		} catch (Exception e) {
			throw new Exception("Error al guardar los medicamentos del tratamiento " + e.getMessage());
		}
		
		//Guardar la consulta
		try {
			procesoDiagnosticoTratamiento.getConsulta().setFecha(LocalDateTime.now());
			procesoDiagnosticoTratamiento.getConsulta().setDiagnosticos(diagnostico);
			procesoDiagnosticoTratamiento.getConsulta().setTratamientos(tratamiento);
			
			consultaDao.save(procesoDiagnosticoTratamiento.getConsulta());
		} catch (Exception e) {
			throw new Exception("Error al guardar la consulta " + e.getMessage());
		}		
	}
}
