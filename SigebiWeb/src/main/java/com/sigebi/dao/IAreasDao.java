package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Areas;

public interface IAreasDao extends JpaRepository<Areas, Integer>, JpaSpecificationExecutor<Areas>  {
	
	List<Areas> findByCodigo(String codigo);
	
}
