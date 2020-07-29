package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Dependencias;

public interface IDependenciasDao extends JpaRepository<Dependencias, Integer>, JpaSpecificationExecutor<Dependencias>  {
	
	List<Dependencias> findByCodigo(String codigo);

}
