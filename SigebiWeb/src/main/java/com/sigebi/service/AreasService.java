package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Areas;

public interface AreasService {
	
	public List<Areas> findAll();
	
	public Areas findById(int id);
	
	public Areas save(Areas area);
	
	public void delete(int id);
	
	public List<Areas> buscar(Date fromDate, Date toDate, Areas area, Pageable pageable);
	
	
}
