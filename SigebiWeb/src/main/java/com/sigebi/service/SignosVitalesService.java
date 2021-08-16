package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.SignosVitales;

public interface SignosVitalesService{

	public List<SignosVitales> findAll();
	
	public int count();
	
	public SignosVitales findById(int id);	
	
	public SignosVitales save(SignosVitales signoVital) throws Exception;
		
	public void delete(int id);
	
	public List<SignosVitales> buscar(Date fromDate, Date toDate,										
										SignosVitales procedimiento, 
										String orderBy, String orderDir, Pageable pageable);
	
}
