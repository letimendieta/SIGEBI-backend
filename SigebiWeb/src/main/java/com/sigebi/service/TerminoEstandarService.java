package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.TerminoEstandar;

public interface TerminoEstandarService {
	
	public List<TerminoEstandar> findAll();
	
	public int count();
	
	public TerminoEstandar findById(int id);
	
	public TerminoEstandar save(TerminoEstandar terminoEstandar);
	
	public void delete(int id);
	
	public List<TerminoEstandar> buscar(Date fromDate, Date toDate, TerminoEstandar terminoEstandar, String orderBy, String orderDir, Pageable pageable);
	
	
}
