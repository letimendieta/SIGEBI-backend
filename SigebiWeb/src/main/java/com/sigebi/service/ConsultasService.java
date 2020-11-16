package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Consultas;

public interface ConsultasService {
	
	public List<Consultas> findAll();
	
	public int count();
	
	public Consultas findById(int id);
	
	public Consultas save(Consultas consulta);
	
	public void delete(int id);
	
	public List<Consultas> buscar(Date fromDate, Date toDate, Consultas consulta, String orderBy, String orderDir, Pageable pageable);
	
	
}
