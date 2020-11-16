package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Anamnesis;

public interface AnamnesisService {
	
	public List<Anamnesis> findAll();
	
	public int count();
	
	public Anamnesis findById(int id);
	
	public Anamnesis save(Anamnesis anamnesis);
	
	public void delete(int id);
	
	public List<Anamnesis> buscar(Date fromDate, Date toDate, Anamnesis anamnesis, String orderBy, String orderDir, Pageable pageable);
	
	
}
