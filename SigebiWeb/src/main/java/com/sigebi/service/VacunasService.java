package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Vacunas;

public interface VacunasService {
	
	public List<Vacunas> findAll();
	
	public int count();
	
	public Vacunas findById(int id);
	
	public Vacunas save(Vacunas vacuna);
	
	public void delete(int id);
	
	public List<Vacunas> buscar(Date fromDate, Date toDate, Vacunas vacuna, String orderBy, String orderDir, Pageable pageable);
	
	
}
