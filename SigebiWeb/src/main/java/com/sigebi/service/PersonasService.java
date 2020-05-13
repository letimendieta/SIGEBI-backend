package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Personas;

public interface PersonasService {
	
	public List<Personas> findAll();
	
	public Personas findById(int id);
	
	public Personas save(Personas cliente);
	
	public void delete(int id);
	
	List<Personas> buscar(Date fromDate, Date toDate, Personas persona, Pageable pageable);
	
	
}
