package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.ProcedimientosInsumos;

public interface IProcedimientosInsumosDao extends JpaRepository<ProcedimientosInsumos, Integer>, JpaSpecificationExecutor<ProcedimientosInsumos>  {
		
}
