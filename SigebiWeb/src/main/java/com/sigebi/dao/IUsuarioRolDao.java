package com.sigebi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sigebi.entity.UsuarioRol;
import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;

public interface IUsuarioRolDao extends JpaRepository<UsuarioRol, Integer>, JpaSpecificationExecutor<UsuarioRol> {
	List<UsuarioRol> findByUsuario(Usuario usuario);	
	List<UsuarioRol> findByRol(Rol rol);
}
