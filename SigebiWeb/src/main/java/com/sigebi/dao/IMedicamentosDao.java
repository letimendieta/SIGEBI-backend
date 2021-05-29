package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Medicamentos;

public interface IMedicamentosDao extends JpaRepository<Medicamentos, Integer>, JpaSpecificationExecutor<Medicamentos>  {

}
