package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Carreras;

public interface CarrerasService {
	
	public List<Carreras> findAll();
	
	public Carreras findById(int id);
	
	public Carreras save(Carreras carrera);
	
	public void delete(int id);
	
	public List<Carreras> buscar(Date fromDate, Date toDate, Carreras carrera, String orderBy, String orderDir, Pageable pageable);
	
	
}
