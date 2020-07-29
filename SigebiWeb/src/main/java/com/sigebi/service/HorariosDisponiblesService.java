package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.HorariosDisponibles;

public interface HorariosDisponiblesService{

	public List<HorariosDisponibles> findAll();
	
	public HorariosDisponibles findById(int id);	
	
	public HorariosDisponibles guardar(HorariosDisponibles horariosDisponible) throws Exception;
	
	public HorariosDisponibles actualizar(HorariosDisponibles horariosDisponible) throws Exception;
	
	public void delete(int id);
	
	public List<HorariosDisponibles> buscar(Date fromDate, Date toDate, 
										HorariosDisponibles horariosDisponible, 
										List<Integer> funcionariosId,
										Pageable pageable);
	
}
