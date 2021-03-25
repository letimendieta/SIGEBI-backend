package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Vacunaciones;

public interface VacunacionesService {
	
	public List<Vacunaciones> findAll();
	
	public int count();
	
	public Vacunaciones findById(int id);
	
	public Vacunaciones save(Vacunaciones vacunacion);
	
	public void delete(int id);
	
	public List<Vacunaciones> buscar(Date fromDate, Date toDate, Vacunaciones vacunacion, String orderBy, String orderDir, Pageable pageable);
	
	
}
