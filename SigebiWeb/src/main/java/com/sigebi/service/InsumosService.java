package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Insumos;

public interface InsumosService {
	
	public List<Insumos> findAll();
	
	public Insumos findById(int id);
	
	public Insumos save(Insumos insumo);
	
	public void delete(int id);
	
	public List<Insumos> buscar(Date fromDate, Date toDate, Insumos insumo, Pageable pageable);
	
	
}
