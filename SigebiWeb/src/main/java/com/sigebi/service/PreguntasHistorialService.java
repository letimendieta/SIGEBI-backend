package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.PreguntasHistorial;

public interface PreguntasHistorialService {
	
	public List<PreguntasHistorial> findAll();
	
	public int count();
	
	public PreguntasHistorial findById(int id);
	
	public PreguntasHistorial save(PreguntasHistorial preguntaHistorial);
	
	public void delete(int id);
	
	public List<PreguntasHistorial> buscar(Date fromDate, Date toDate, PreguntasHistorial preguntaHistorial, String orderBy, String orderDir, Pageable pageable);
	
	
}
