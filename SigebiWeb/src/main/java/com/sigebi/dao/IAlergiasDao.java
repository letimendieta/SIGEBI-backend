package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Alergias;

public interface IAlergiasDao extends JpaRepository<Alergias, Integer>, JpaSpecificationExecutor<Alergias> {
	
	List<Alergias> findByPacienteId(Integer pacienteId);
}
