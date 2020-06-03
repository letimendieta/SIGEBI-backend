package com.sigebi.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

@Entity
public class Pacientes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_pacientes")
	@SequenceGenerator(name="seq_pacientes",sequenceName="seq_pacientes",allocationSize=1)
	private Integer pacienteId;
	
	@Column(name = "historial_id")
	private Integer historialId;
	
	@Column(name = "grupo_sanguineo", length = 5)
	@Size(max=5, message="maximo 5 caracteres")
	private String grupoSanguineo;
	
	@Column(name = "seguro_medico", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String seguroMedico;
	
	@Column(name = "fecha_creacion")
	private Timestamp fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private Timestamp fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioModificacion;
	
	@OneToOne
    @JoinColumn(name = "persona_id", referencedColumnName = "personaId", unique = true)
    private Personas personas;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = new Timestamp(System.currentTimeMillis());
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = new Timestamp(System.currentTimeMillis());
	}

	public Integer getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
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

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public Timestamp getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Timestamp fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	public Personas getPersonas() {
		return personas;
	}

	public void setPersonas(Personas personas) {
		this.personas = personas;
	}

	
}
