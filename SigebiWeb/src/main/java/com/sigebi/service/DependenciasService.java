package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Dependencias;

public interface DependenciasService {
	
	public List<Dependencias> findAll();
	
	public Dependencias findById(int id);
	
	public int count();
	
	public Dependencias save(Dependencias dependencia);
	
	public void delete(int id);
	
	public List<Dependencias> buscar(Date fromDate, Date toDate, Dependencias dependencia, String orderBy, String orderDir, Pageable pageable);
	
	
}
