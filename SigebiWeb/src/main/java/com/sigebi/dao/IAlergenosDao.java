package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Alergenos;

public interface IAlergenosDao extends JpaRepository<Alergenos, Integer>, JpaSpecificationExecutor<Alergenos>  {
	
	List<Alergenos> findByCodigo(String codigo);
	
}
