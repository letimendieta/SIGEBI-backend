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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "termino_estandar")
public class TerminoEstandar {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_termino_estandar")
	@SequenceGenerator(name="seq_termino_estandar",sequenceName="seq_termino_estandar",allocationSize=1)
	private Integer id;
	
	@Column(name = "codigo_unico", length = 10, unique = true)
	@NotEmpty(message ="no puede estar vacio")
	@Size(max=10, message="maximo 10 caracteres")
	private String codigoUnico;
	
	@Column(name = "termino", length = 300)
	@Size(max=300, message="maximo 300 caracteres")
	private String termino;	
	
	@Column(name = "estandar_terminologia_id")
	private Integer estandarTerminologiaId;
	
	@Column(name = "contexto_id")
	private Integer contextoId;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoUnico() {
		return codigoUnico;
	}

	public void setCodigoUnico(String codigoUnico) {
		this.codigoUnico = codigoUnico;
	}

	public String getTermino() {
		return termino;
	}

	public void setTermino(String termino) {
		this.termino = termino;
	}

	public Integer getEstandarTerminologiaId() {
		return estandarTerminologiaId;
	}

	public void setEstandarTerminologiaId(Integer estandarTerminologiaId) {
		this.estandarTerminologiaId = estandarTerminologiaId;
	}

	public Integer getContextoId() {
		return contextoId;
	}

	public void setContextoId(Integer contextoId) {
		this.contextoId = contextoId;
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
