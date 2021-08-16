package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Alergias;

public interface AlergiasService{

	public List<Alergias> findAll();
	
	public Alergias findById(int id);	
	
	public int count();
	
	public Alergias guardar(Alergias alergia) throws Exception;
	
	public Alergias actualizar(Alergias alergia) throws Exception;
	
	public void delete(int id);
	
	public List<Alergias> buscar(Date fromDate, Date toDate, Alergias alergia, String orderBy, String orderDir, Pageable pageable);
	
}
