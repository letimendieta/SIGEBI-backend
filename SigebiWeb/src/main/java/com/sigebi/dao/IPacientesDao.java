package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;

public interface IPacientesDao extends JpaRepository<Pacientes, Integer>, JpaSpecificationExecutor<Pacientes> {
	
	List<Pacientes> findByPersonas(Personas personas);	
	Pacientes findByPacienteId(Integer pacienteId);
}
