package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.entity.Funcionarios;

public interface IFuncionariosDao extends JpaRepository<Funcionarios, Integer> {

}
