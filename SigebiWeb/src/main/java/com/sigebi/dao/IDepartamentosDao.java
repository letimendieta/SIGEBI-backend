package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Departamentos;

public interface IDepartamentosDao extends JpaRepository<Departamentos, Integer>, JpaSpecificationExecutor<Departamentos>  {
	
	List<Departamentos> findByCodigo(String codigo);
	
}
