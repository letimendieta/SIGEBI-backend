package com.sigebi.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class HistorialClinico {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer historialClinicoId;
	
	@Column(name = "paciente_id")
	@NotEmpty(message ="no puede estar vacio")
	private Integer paciente_id;
	
	@Column(name = "area_id")
	private Integer areaId;
	
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
	
	@PrePersist
	private void create() {
		this.fechaCreacion = new Timestamp(System.currentTimeMillis());
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = new Timestamp(System.currentTimeMillis());
	}

	public Integer getHistorialClinicoId() {
		return historialClinicoId;
	}

	public void setHistorialClinicoId(Integer historialClinicoId) {
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
	
	
}
