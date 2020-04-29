package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dependencias {
	
	@Id
	private int dependenciaId;
	
	@Column(name = "codigo", length = 15)
	private String codigo;
	
	@Column(name = "descripcion", length = 30)
	private String descripcion;

	public int getDependenciaId() {
		return dependenciaId;
	}

	public void setDependenciaId(int dependenciaId) {
		this.dependenciaId = dependenciaId;
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
