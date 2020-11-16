package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Tratamientos;

public interface TratamientosService {
	
	public List<Tratamientos> findAll();
	
	public int count();
	
	public Tratamientos findById(int id);
	
	public Tratamientos save(Tratamientos tratamiento);
	
	public void delete(int id);
	
	public List<Tratamientos> buscar(Date fromDate, Date toDate, Tratamientos tratamiento, String orderBy, String orderDir, Pageable pageable);
	
	
}
