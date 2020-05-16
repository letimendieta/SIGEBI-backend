package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Pacientes;

public interface PacientesService{

	public List<Pacientes> findAll();
	
	public Pacientes findById(int id);
	
	public Pacientes save(Pacientes paciente);
	
	public void delete(int id);
	
	public List<Pacientes> buscar(Date fromDate, Date toDate, Pacientes paciente, Pageable pageable);

}
