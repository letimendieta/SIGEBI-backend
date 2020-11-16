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
@Table(name = "tratamientos")
public class Tratamientos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_tratamientos")
	@SequenceGenerator(name="seq_tratamientos",sequenceName="seq_tratamientos",allocationSize=1)
	private Integer tratamientoId;
	
	@Column(name = "consulta_id")
	private Integer consultaId;
	
	@Column(name = "prescripcion_farm", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String prescripcionFarm;
	
	@Column(name = "descripcion_tratamiento", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String descripcionTratamiento;	
		
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
	
	public Integer getTratamientoId() {
		return tratamientoId;
	}

	public void setTratamientoId(Integer tratamientoId) {
		this.tratamientoId = tratamientoId;
	}

	public Integer getConsultaId() {
		return consultaId;
	}

	public void setConsultaId(Integer consultaId) {
		this.consultaId = consultaId;
	}

	public String getPrescripcionFarm() {
		return prescripcionFarm;
	}

	public void setPrescripcionFarm(String prescripcionFarm) {
		this.prescripcionFarm = prescripcionFarm;
	}

	public String getDescripcionTratamiento() {
		return descripcionTratamiento;
	}

	public void setDescripcionTratamiento(String descripcionTratamiento) {
		this.descripcionTratamiento = descripcionTratamiento;
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
