package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Consultas;

public interface IConsultasDao extends JpaRepository<Consultas, Integer>, JpaSpecificationExecutor<Consultas>  {
}
