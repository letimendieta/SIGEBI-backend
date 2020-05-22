package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Pacientes;

public interface IPacientesDao extends JpaRepository<Pacientes, Integer>, JpaSpecificationExecutor<Pacientes> {

}
