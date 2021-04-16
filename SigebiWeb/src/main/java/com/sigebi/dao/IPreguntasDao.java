package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Preguntas;

public interface IPreguntasDao extends JpaRepository<Preguntas, Integer>, JpaSpecificationExecutor<Preguntas>  {
		
}
