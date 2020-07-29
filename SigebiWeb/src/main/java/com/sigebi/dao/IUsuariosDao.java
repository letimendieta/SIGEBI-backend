package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.entity.Usuarios;

public interface IUsuariosDao extends JpaRepository<Usuarios, Integer>, JpaSpecificationExecutor<Usuarios> {
	
	Usuarios findByCodigoUsuario(String codigoUsuario);	
	List<Usuarios> findByPersonas(Personas personas);	
	List<Usuarios> findByFuncionarios(Funcionarios funcionarios);
}
