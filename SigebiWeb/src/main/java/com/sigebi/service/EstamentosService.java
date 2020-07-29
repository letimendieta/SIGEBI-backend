package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Estamentos;

public interface EstamentosService {
	
	public List<Estamentos> findAll();
	
	public Estamentos findById(int id);
	
	public Estamentos save(Estamentos estamento);
	
	public void delete(int id);
	
	public List<Estamentos> buscar(Date fromDate, Date toDate, Estamentos estamento, String orderBy, String orderDir, Pageable pageable);
	
	
}
