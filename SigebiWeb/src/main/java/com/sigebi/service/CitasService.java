package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Citas;

public interface CitasService{

	public List<Citas> findAll();
	
	public Citas findById(int id);	
	
	public int count();
	
	public Citas guardar(Citas cita) throws Exception;
	
	public Citas actualizar(Citas cita) throws Exception;
	
	public void delete(int id);
	
	public List<Citas> buscar(Date fromDate, Date toDate, 
										Citas cita, 
										List<Integer> funcionariosId,
										List<Integer> pacientesId, String orderBy, String orderDir, Pageable pageable);
	
}
