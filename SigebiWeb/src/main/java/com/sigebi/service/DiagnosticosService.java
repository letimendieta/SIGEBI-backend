package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Diagnosticos;

public interface DiagnosticosService {
	
	public List<Diagnosticos> findAll();
	
	public int count();
	
	public Diagnosticos findById(int id);
	
	public Diagnosticos save(Diagnosticos diagnostico);
	
	public void delete(int id);
	
	public List<Diagnosticos> buscar(Date fromDate, Date toDate, Diagnosticos diagnostico, String orderBy, String orderDir, Pageable pageable);
	
	
}
