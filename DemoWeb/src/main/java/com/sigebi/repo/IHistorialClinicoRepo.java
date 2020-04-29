package com.sigebi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.HistorialClinico;

public interface IHistorialClinicoRepo extends JpaRepository<HistorialClinico, Integer> {

}
