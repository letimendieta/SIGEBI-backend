package com.sigebi.clases;

import java.io.Serializable;

import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;

public class UsuarioRolId implements Serializable {
	
	private Usuario usuario;	
	
	private Rol rol;
		
	public UsuarioRolId() {
		super();
	}

	public UsuarioRolId(Usuario usuario, Rol rol) {
		this.usuario = usuario;
		this.rol = rol;
	}	
}
