package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Citas;

public interface ICitasDao extends JpaRepository<Citas, Integer>, JpaSpecificationExecutor<Citas> {

}
