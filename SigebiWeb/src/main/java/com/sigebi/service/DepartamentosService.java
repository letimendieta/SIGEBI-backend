package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Departamentos;

public interface DepartamentosService {
	
	public List<Departamentos> findAll();
	
	public Departamentos findById(int id);
	
	public int count();
	
	public Departamentos save(Departamentos departamento);
	
	public void delete(int id);
	
	public List<Departamentos> buscar(Date fromDate, Date toDate, Departamentos departamento, String orderBy, String orderDir, Pageable pageable);
	
	
}
