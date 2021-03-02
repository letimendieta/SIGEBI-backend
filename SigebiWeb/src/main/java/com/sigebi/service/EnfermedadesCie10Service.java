package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.EnfermedadesCie10;

public interface EnfermedadesCie10Service {
	
	public List<EnfermedadesCie10> findAll();
	
	public int count();
	
	public EnfermedadesCie10 findById(int id);
	
	public EnfermedadesCie10 save(EnfermedadesCie10 enfermedadCie10);
	
	public void delete(int id);
	
	public List<EnfermedadesCie10> buscar(Date fromDate, Date toDate, EnfermedadesCie10 enfermedadCie10, String orderBy, String orderDir, Pageable pageable);
	
	
}
