package com.sigebi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Funcionarios;

public interface IFuncionariosRepo extends JpaRepository<Funcionarios, Integer> {

}
