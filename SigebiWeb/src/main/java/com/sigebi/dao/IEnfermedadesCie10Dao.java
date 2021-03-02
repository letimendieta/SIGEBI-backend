package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.EnfermedadesCie10;

public interface IEnfermedadesCie10Dao extends JpaRepository<EnfermedadesCie10, Integer>, JpaSpecificationExecutor<EnfermedadesCie10>  {
	
	List<EnfermedadesCie10> findByCodigo(String codigo);
	
}
