package com.sigebi.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Funcionarios {

	@Id
	private int funcionarioId;
	
	@Column(name = "persona_id")
	private Integer personaId;
	
	@Column(name = "area_id")
	private Integer areaId;
	
	@Column(name = "fecha_ingreso")
	private Date fechaIngreso;
	
	@Column(name = "fecha_egreso")
	private Date fechaEgreso;	
	
	@Column(name = "estado", length = 1)
	private String estado;

	public int getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(int funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public Date getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(Date fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
}
