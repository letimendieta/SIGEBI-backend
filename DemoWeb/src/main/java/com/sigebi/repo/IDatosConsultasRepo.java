package com.sigebi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.DatosConsultas;

public interface IDatosConsultasRepo extends JpaRepository<DatosConsultas, Integer> {

}
