package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Insumos;
import com.sigebi.entity.Stock;

public interface IStockDao extends JpaRepository<Stock, Integer>, JpaSpecificationExecutor<Stock>  {
	
	Stock findByInsumos(Insumos insumos);
	
}
