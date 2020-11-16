package com.sigebi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "anamnesis")
public class Anamnesis {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_anamnesis")
	@SequenceGenerator(name="seq_anamnesis",sequenceName="seq_anamnesis",allocationSize=1)
	private Integer anamnesisId;
	
	@Column(name = "antecedentes", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String antecedentes;
	
	@Column(name = "antecedentes_remotos", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String antecedentesRemotos;
	
	@Column(name = "historial_clinico_id")
	private Integer historialClinicoId;
			
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	private String usuarioModificacion;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	

	public Integer getAnamnesisId() {
		return anamnesisId;
	}

	public void setAnamnesisId(Integer anamnesisId) {
		this.anamnesisId = anamnesisId;
	}

	public String getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(String antecedentes) {
		this.antecedentes = antecedentes;
	}

	public String getAntecedentesRemotos() {
		return antecedentesRemotos;
	}

	public void setAntecedentesRemotos(String antecedentesRemotos) {
		this.antecedentesRemotos = antecedentesRemotos;
	}

	public Integer getHistorialClinicoId() {
		return historialClinicoId;
	}

	public void setHistorialClinicoId(Integer historialClinicoId) {
		this.historialClinicoId = historialClinicoId;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}
	
	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}	
	
	
	
}
