package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.SignosVitales;

public interface ISignosVitalesDao extends JpaRepository<SignosVitales, Integer>, JpaSpecificationExecutor<SignosVitales> {

}
