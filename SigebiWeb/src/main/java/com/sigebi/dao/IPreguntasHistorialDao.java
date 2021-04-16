package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.PreguntasHistorial;

public interface IPreguntasHistorialDao extends JpaRepository<PreguntasHistorial, Integer>, JpaSpecificationExecutor<PreguntasHistorial>  {
		
	List<PreguntasHistorial> findByHistorialClinicoId(Integer historialClinicoId);
}
