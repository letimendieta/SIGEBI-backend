package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Personas;

public interface IPersonasDao extends JpaRepository<Personas, Integer>, JpaSpecificationExecutor<Personas>  {
	
	List<Personas> findByNombres(String nombres);
	
	List<Personas> findByCedula(String cedula);
	
}
