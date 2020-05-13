package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.entity.Pacientes;

public interface IPacientesDao extends JpaRepository<Pacientes, Integer> {

}
