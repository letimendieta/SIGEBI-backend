package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Carreras;

public interface ICarrerasDao extends JpaRepository<Carreras, Integer>, JpaSpecificationExecutor<Carreras>  {
	
	List<Carreras> findByCodigo(String codigo);
}
