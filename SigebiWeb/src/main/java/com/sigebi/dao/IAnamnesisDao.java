package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Anamnesis;

public interface IAnamnesisDao extends JpaRepository<Anamnesis, Integer>, JpaSpecificationExecutor<Anamnesis>  {
		
}
