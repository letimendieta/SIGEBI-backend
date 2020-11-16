package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.TratamientosInsumos;

public interface TratamientosInsumosService {
	
	public List<TratamientosInsumos> findAll();
	
	public int count();
	
	public TratamientosInsumos findById(int id);
	
	public TratamientosInsumos save(TratamientosInsumos tratamientoInsumo);
	
	public void delete(int id);
	
	public List<TratamientosInsumos> buscar(Date fromDate, Date toDate, TratamientosInsumos tratamientoInsumo, String orderBy, String orderDir, Pageable pageable);
	
	
}
