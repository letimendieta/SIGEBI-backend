package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.security.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {
	//Usuario findByPersonas(Personas personas);	
	Usuario findByFuncionarios(Funcionarios funcionarios);
}
