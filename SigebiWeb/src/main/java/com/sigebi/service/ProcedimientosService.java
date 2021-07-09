package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.clases.ProcesoProcedimientos;
import com.sigebi.entity.Procedimientos;
import com.sigebi.entity.ProcedimientosInsumos;

public interface ProcedimientosService{

	public List<Procedimientos> findAll();
	
	public Procedimientos findById(int id);
	
	public List<Procedimientos> obtenerProcedimientoPaciente(int pacienteId);
	
	public Procedimientos guardar(Procedimientos procedimiento);
	
	public Procedimientos guardar(ProcesoProcedimientos Procesoprocedimiento) throws Exception;
		
	public Procedimientos actualizarProcesoProcedimientos(ProcesoProcedimientos Procesoprocedimiento) throws Exception;
	
	public void delete(int id);
	
	public List<Procedimientos> buscar(Date fromDate, Date toDate, 
										Procedimientos procedimiento, 
										List<Integer> funcionariosId,
										List<Integer> pacientesId,
										Pageable pageable);
	
	public List<Procedimientos> buscarNoPaginable(Date fromDate, Date toDate, 
			Procedimientos procedimiento, 
			List<Integer> funcionariosId,
			List<Integer> pacientesId);
	
}
