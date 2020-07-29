package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Estamentos;

public interface IEstamentosDao extends JpaRepository<Estamentos, Integer>, JpaSpecificationExecutor<Estamentos>  {
	
	List<Estamentos> findByCodigo(String codigo);

}
