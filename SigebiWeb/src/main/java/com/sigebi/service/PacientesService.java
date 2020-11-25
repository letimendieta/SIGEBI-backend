package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Pacientes;

public interface PacientesService{

	public List<Pacientes> findAll();
	
	public Pacientes findById(int id);	
		
	public Pacientes guardar(Pacientes paciente) throws Exception;
	
	public Pacientes actualizar(Pacientes paciente) throws Exception;
	
	public void delete(int id);
	
	public List<Pacientes> buscar(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId, Pageable pageable);
	
	public List<Pacientes> buscarNoPaginable(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId);
	
}
