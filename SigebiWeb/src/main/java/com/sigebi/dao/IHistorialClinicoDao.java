package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.HistorialClinico;

public interface IHistorialClinicoDao extends JpaRepository<HistorialClinico, Integer>, JpaSpecificationExecutor<HistorialClinico> {

}
