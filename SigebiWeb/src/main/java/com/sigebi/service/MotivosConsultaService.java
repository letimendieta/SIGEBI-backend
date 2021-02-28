package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.MotivosConsulta;

public interface MotivosConsultaService {
	
	public List<MotivosConsulta> findAll();
	
	public int count();
	
	public MotivosConsulta findById(int id);
	
	public MotivosConsulta save(MotivosConsulta motivoConsulta);
	
	public void delete(int id);
	
	public List<MotivosConsulta> buscar(Date fromDate, Date toDate, MotivosConsulta motivoConsulta, String orderBy, String orderDir, Pageable pageable);
	
	
}
