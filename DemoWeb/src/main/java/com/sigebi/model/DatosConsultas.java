package com.sigebi.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DatosConsultas {
	
	@Id
	private int datoConsultaId;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "descripcion", length = 500)
	private String descripcion;
	
	@Column(name = "historial_clinico_id")
	private Integer historialClinicoId;

	public int getDatoConsultaId() {
		return datoConsultaId;
	}

	public void setDatoConsultaId(int datoConsultaId) {
		this.datoConsultaId = datoConsultaId;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getHistorialClinicoId() {
		return historialClinicoId;
	}

	public void setHistorialClinicoId(Integer historialClinicoId) {
		this.historialClinicoId = historialClinicoId;
	}
	
	
}
