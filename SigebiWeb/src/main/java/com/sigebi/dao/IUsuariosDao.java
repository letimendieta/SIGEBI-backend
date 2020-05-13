package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.entity.Usuarios;

public interface IUsuariosDao extends JpaRepository<Usuarios, Integer>{
	
	Usuarios findByCodigoUsuario(String codigoUsuario);
}
