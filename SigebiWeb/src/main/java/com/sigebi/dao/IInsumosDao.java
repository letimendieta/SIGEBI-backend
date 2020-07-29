package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Insumos;

public interface IInsumosDao extends JpaRepository<Insumos, Integer>, JpaSpecificationExecutor<Insumos>  {

}
