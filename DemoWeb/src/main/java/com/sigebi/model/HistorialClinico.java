package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HistorialClinico {
	
	@Id
	private int historialClinicoId;
	
	@Column(name = "paciente_id")
	private Integer paciente_id;
	
	@Column(name = "area_id")
	private Integer areaId;

	public int getHistorialClinicoId() {
		return historialClinicoId;
	}

	public void setHistorialClinicoId(int historialClinicoId) {
		this.historialClinicoId = historialClinicoId;
	}

	public Integer getPaciente_id() {
		return paciente_id;
	}

	public void setPaciente_id(Integer paciente_id) {
		this.paciente_id = paciente_id;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	
	
}
