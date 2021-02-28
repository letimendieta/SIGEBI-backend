package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.clases.HistorialClinicoPaciente;
import com.sigebi.entity.HistorialClinico;

public interface HistorialesClinicosService{

	public List<HistorialClinico> findAll();
	
	public HistorialClinico findById(int id);	
	
	public HistorialClinico guardar(HistorialClinico historialClinico) throws Exception;
	
	public HistorialClinico actualizar(HistorialClinico historialClinico) throws Exception;
	
	public void delete(int id);
	
	public List<HistorialClinico> buscar(Date fromDate, Date toDate, 
										HistorialClinico historialClinico, 
										Pageable pageable);
	
	public List<HistorialClinico> buscarNoPaginable(Date fromDate, Date toDate, 
			HistorialClinico historialClinico);
	
}
