package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Parametros;

public interface IParametrosDao extends JpaRepository<Parametros, Integer>, JpaSpecificationExecutor<Parametros>  {

}
