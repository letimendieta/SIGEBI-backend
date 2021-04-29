package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Procedimientos;

public interface IProcedimientosDao extends JpaRepository<Procedimientos, Integer>, JpaSpecificationExecutor<Procedimientos> {
	
	
}
