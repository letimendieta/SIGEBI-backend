package com.sigebi.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Pacientes;

public interface IPacientesRepo extends JpaRepository<Pacientes, Integer> {

}
