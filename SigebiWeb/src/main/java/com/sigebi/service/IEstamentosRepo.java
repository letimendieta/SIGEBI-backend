package com.sigebi.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Estamentos;

public interface IEstamentosRepo extends JpaRepository<Estamentos, Integer> {

}
