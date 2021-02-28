package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Alergenos;

public interface AlergenosService {
	
	public List<Alergenos> findAll();
	
	public int count();
	
	public Alergenos findById(int id);
	
	public Alergenos save(Alergenos alergeno);
	
	public void delete(int id);
	
	public List<Alergenos> buscar(Date fromDate, Date toDate, Alergenos alergeno, String orderBy, String orderDir, Pageable pageable);
	
	
}
