package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Carreras {
	
	@Id
	private int carreraId;
	
	@Column(name = "codigo", length = 15)
	private String codigo;
	
	@Column(name = "descripcion", length = 30)
	private String descripcion;

	public int getCarreraId() {
		return carreraId;
	}

	public void setCarreraId(int carreraId) {
		this.carreraId = carreraId;
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
