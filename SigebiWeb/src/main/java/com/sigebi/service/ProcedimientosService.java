package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Procedimientos;
import com.sigebi.entity.ProcesoProcedimientos;

public interface ProcedimientosService{

	public List<Procedimientos> findAll();
	
	public Procedimientos findById(int id);	
	
	public Procedimientos guardar(ProcesoProcedimientos Procesoprocedimiento) throws Exception;
	
	public Procedimientos actualizar(ProcesoProcedimientos Procesoprocedimiento) throws Exception;
	
	public void delete(int id);
	
	public List<Procedimientos> buscar(Date fromDate, Date toDate, 
										Procedimientos procedimiento, 
										List<Integer> funcionariosId,
										List<Integer> pacientesId,
										Pageable pageable);
	
}
