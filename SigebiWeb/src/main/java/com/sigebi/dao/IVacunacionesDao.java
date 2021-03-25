package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Vacunaciones;

public interface IVacunacionesDao extends JpaRepository<Vacunaciones, Integer>, JpaSpecificationExecutor<Vacunaciones>  {
		
}
