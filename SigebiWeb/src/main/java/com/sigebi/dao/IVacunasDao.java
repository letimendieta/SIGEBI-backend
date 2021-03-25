package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Vacunas;

public interface IVacunasDao extends JpaRepository<Vacunas, Integer>, JpaSpecificationExecutor<Vacunas>  {
	
	List<Vacunas> findByCodigo(String codigo);
	
}
