package com.sigebi.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Usuarios;

public interface IUsuariosRepo extends JpaRepository<Usuarios, Integer>{
	
	Usuarios findByCodigoUsuario(String codigoUsuario);
}
