package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.MovimientosInsumos;

public interface IMovimientoInsumoDao extends JpaRepository<MovimientosInsumos, Integer>, JpaSpecificationExecutor<MovimientosInsumos>  {
		
}
