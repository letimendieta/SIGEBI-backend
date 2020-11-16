package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Antecedentes;

public interface AntecedentesService{

	public List<Antecedentes> findAll();
	
	public Antecedentes findById(int id);	
	
	public Antecedentes guardar(Antecedentes antecedente) throws Exception;
	
	public Antecedentes actualizar(Antecedentes antecedente) throws Exception;
	
	public void delete(int id);
	
	public List<Antecedentes> buscar(Date fromDate, Date toDate, Antecedentes antecedente, 
										Pageable pageable);
	
}
