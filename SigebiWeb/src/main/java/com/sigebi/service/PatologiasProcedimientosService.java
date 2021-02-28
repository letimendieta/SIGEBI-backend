package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.PatologiasProcedimientos;

public interface PatologiasProcedimientosService {
	
	public List<PatologiasProcedimientos> findAll();
	
	public int count();
	
	public PatologiasProcedimientos findById(int id);
	
	public PatologiasProcedimientos save(PatologiasProcedimientos patologiaProcedimiento);
	
	public void delete(int id);
	
	public List<PatologiasProcedimientos> buscar(Date fromDate, Date toDate, PatologiasProcedimientos patologiaProcedimiento, String orderBy, String orderDir, Pageable pageable);
	
	
}
