package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Areas {
	
	@Id
	private int areaId;
	
	@Column(name = "codigo", length = 15)
	private String codigo;
	
	@Column(name = "descripcion", length = 30)
	private String descripcion;	
	
	@Column(name = "estado", length = 1)
	private String estado;

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}	
	
	
	
}
