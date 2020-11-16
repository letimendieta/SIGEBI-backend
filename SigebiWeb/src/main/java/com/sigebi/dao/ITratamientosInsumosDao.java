package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.TratamientosInsumos;

public interface ITratamientosInsumosDao extends JpaRepository<TratamientosInsumos, Integer>, JpaSpecificationExecutor<TratamientosInsumos>  {
		
}
