package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Permisos {
	
	@Id
	private int permiso_id;
	
	@Column(name = "codigo", length = 15)
	private String codigo;
	
	@Column(name = "descripcion", length = 30)
	private String descripcion;

	public int getPermiso_id() {
		return permiso_id;
	}

	public void setPermiso_id(int permiso_id) {
		this.permiso_id = permiso_id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
