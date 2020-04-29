package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UsuariosRoles {
	
	@Id
	private int usuarioRolId;
	
	@Column(name = "usuario_id")
	private Integer usuarioId;
	
	@Column(name = "rol_id")
	private Integer rolId;

	public int getUsuarioRolId() {
		return usuarioRolId;
	}

	public void setUsuarioRolId(int usuarioRolId) {
		this.usuarioRolId = usuarioRolId;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Integer getRolId() {
		return rolId;
	}

	public void setRolId(Integer rolId) {
		this.rolId = rolId;
	}
	
	
}
