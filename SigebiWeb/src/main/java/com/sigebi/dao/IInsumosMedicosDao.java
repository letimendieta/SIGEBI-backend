package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.InsumosMedicos;

public interface IInsumosMedicosDao extends JpaRepository<InsumosMedicos, Integer>, JpaSpecificationExecutor<InsumosMedicos>  {

}
