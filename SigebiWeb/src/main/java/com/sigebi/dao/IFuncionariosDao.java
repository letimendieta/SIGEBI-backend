package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;

public interface IFuncionariosDao extends JpaRepository<Funcionarios, Integer>, JpaSpecificationExecutor<Funcionarios> {
	List<Funcionarios> findByPersonas(Personas personas);
}
