package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.MotivosConsulta;

public interface IMotivosConsultaDao extends JpaRepository<MotivosConsulta, Integer>, JpaSpecificationExecutor<MotivosConsulta>  {
	
	List<MotivosConsulta> findByCodigo(String codigo);
	
}
