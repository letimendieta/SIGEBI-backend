package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Estamentos {
	
	@Id
	private int estamentoId;
	
	@Column(name = "codigo", length = 15)
	private String codigo;
	
	@Column(name = "descripcion", length = 30)
	private String descripcion;

	public int getEstamentoId() {
		return estamentoId;
	}

	public void setEstamentoId(int estamentoId) {
		this.estamentoId = estamentoId;
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
