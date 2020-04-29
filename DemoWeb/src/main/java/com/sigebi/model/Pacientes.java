package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Pacientes {
	
	@Id
	private Integer pacienteId;
	
	@Column(name = "persona_id")
	private Integer personaId;
	
	@Column(name = "historial_id")
	private Integer historialId;
	
	@Column(name = "grupo_sanguineo", length = 5)
	private String grupoSanguineo;
	
	@Column(name = "seguro_medico", length = 15)
	private String seguroMedico;

	public Integer getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public Integer getHistorialId() {
		return historialId;
	}

	public void setHistorialId(Integer historialId) {
		this.historialId = historialId;
	}

	public String getGrupoSanguineo() {
		return grupoSanguineo;
	}

	public void setGrupoSanguineo(String grupoSanguineo) {
		this.grupoSanguineo = grupoSanguineo;
	}

	public String getSeguroMedico() {
		return seguroMedico;
	}

	public void setSeguroMedico(String seguroMedico) {
		this.seguroMedico = seguroMedico;
	}
	
	
}
