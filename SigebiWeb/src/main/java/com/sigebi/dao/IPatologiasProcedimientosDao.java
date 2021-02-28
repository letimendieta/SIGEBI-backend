package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.PatologiasProcedimientos;

public interface IPatologiasProcedimientosDao extends JpaRepository<PatologiasProcedimientos, Integer>, JpaSpecificationExecutor<PatologiasProcedimientos>  {
	
	List<PatologiasProcedimientos> findByCodigo(String codigo);
	
}
