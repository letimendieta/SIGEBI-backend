package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Tratamientos;

public interface ITratamientosDao extends JpaRepository<Tratamientos, Integer>, JpaSpecificationExecutor<Tratamientos>  {

	
}
