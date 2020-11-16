package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Diagnosticos;

public interface IDiagnosticosDao extends JpaRepository<Diagnosticos, Integer>, JpaSpecificationExecutor<Diagnosticos>  {
	
	
}
