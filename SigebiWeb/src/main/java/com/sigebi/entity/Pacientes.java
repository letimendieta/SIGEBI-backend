package com.sigebi.entity;

import java.time.LocalDateTime;

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
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pacientes")
public class Pacientes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_pacientes")
	@SequenceGenerator(name="seq_pacientes",sequenceName="seq_pacientes",allocationSize=1)
	private Integer pacienteId;
	
	@Column(name = "grupo_sanguineo", length = 5)
	@Size(max=5, message="maximo 5 caracteres")
	private String grupoSanguineo;
	
	@Column(name = "seguro_medico", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String seguroMedico;
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioModificacion;
	
	@OneToOne
    @JoinColumn(name = "historial_clinico_id", referencedColumnName = "historialClinicoId", unique = true)
    private HistorialClinico historialClinico;
	
	@OneToOne
    @JoinColumn(name = "persona_id", referencedColumnName = "personaId", unique = true)
    private Personas personas;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}

	public Integer getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
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

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
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

	public HistorialClinico getHistorialClinico() {
		return historialClinico;
	}

	public void setHistorialClinico(HistorialClinico historialClinico) {
		this.historialClinico = historialClinico;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	
}
