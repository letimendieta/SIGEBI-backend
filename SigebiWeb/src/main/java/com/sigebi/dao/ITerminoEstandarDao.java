package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.TerminoEstandar;

public interface ITerminoEstandarDao extends JpaRepository<TerminoEstandar, Integer>, JpaSpecificationExecutor<TerminoEstandar>  {
		
}
