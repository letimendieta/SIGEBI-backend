package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Preguntas;

public interface PreguntasService {
	
	public List<Preguntas> findAll();
	
	public int count();
	
	public Preguntas findById(int id);
	
	public Preguntas save(Preguntas pregunta);
	
	public void delete(int id);
	
	public List<Preguntas> buscar(Date fromDate, Date toDate, Preguntas pregunta, String orderBy, String orderDir, Pageable pageable);
	
	
}
