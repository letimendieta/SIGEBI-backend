package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.entity.DatosConsultas;

public interface IDatosConsultasDao extends JpaRepository<DatosConsultas, Integer> {

}
