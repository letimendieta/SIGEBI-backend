package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Antecedentes;

public interface IAntecedentesDao extends JpaRepository<Antecedentes, Integer>, JpaSpecificationExecutor<Antecedentes> {
	
	List<Antecedentes> findByPacienteId(Integer pacienteId);	
}
