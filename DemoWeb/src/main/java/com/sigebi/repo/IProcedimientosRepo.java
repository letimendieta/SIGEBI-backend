package com.sigebi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Procedimientos;

public interface IProcedimientosRepo extends JpaRepository<Procedimientos, Integer> {

}
