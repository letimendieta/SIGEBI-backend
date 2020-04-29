package com.sigebi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Personas;

public interface IPersonasRepo extends JpaRepository<Personas, Integer> {

}
