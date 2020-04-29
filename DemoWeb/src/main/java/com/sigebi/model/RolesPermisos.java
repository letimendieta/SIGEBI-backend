package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RolesPermisos {
	
	@Id
	private int rolPermisoId;
	
	@Column(name = "rol_id")
	private Integer rolId;
	
	@Column(name = "permiso_id")
	private Integer permisoId;

	public int getRolPermisoId() {
		return rolPermisoId;
	}

	public void setRolPermisoId(int rolPermisoId) {
		this.rolPermisoId = rolPermisoId;
	}

	public Integer getRolId() {
		return rolId;
	}

	public void setRolId(Integer rolId) {
		this.rolId = rolId;
	}

	public Integer getPermisoId() {
		return permisoId;
	}

	public void setPermisoId(Integer permisoId) {
		this.permisoId = permisoId;
	}
	
	
}
