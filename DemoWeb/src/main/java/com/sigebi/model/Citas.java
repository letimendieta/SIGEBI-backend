package com.sigebi.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Citas {
		
	@Id
	private Integer citaId;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "paciente_id")
	private Integer pacienteId;
	
	@Column(name = "funcionario_id")
	private Integer funcionarioId;
	
	@Column(name = "hora")
	private Time hora;
	
	@Column(name = "area_id")
	private Integer areaId;	
	
	@Column(name = "estado", length = 1)
	private String estado;

	public Integer getCitaId() {
		return citaId;
	}

	public void setCitaId(Integer citaId) {
		this.citaId = citaId;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
	}

	public Integer getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(Integer funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
