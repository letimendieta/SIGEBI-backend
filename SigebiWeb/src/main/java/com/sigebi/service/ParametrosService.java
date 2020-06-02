package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Parametros;

public interface ParametrosService {
	
	public List<Parametros> findAll();
	
	public Parametros findById(int id);
	
	public Parametros save(Parametros parametro);
	
	public void delete(int id);
	
	public List<Parametros> buscar(Date fromDate, Date toDate, Parametros parametro, String orderBy, String orderDir, Pageable pageable);
	
	
}
