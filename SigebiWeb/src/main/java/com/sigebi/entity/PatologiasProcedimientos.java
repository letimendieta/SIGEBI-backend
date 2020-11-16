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
@Table(name = "patologias_procedimientos")
public class PatologiasProcedimientos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_patologias_procedimientos")
	@SequenceGenerator(name="seq_patologias_procedimientos",sequenceName="seq_patologias_procedimientos",allocationSize=1)
	private Integer patologiaProcedimientoId;
	
	@Column(name = "codigo", length = 15, unique = true)
	@NotEmpty(message ="no puede estar vacio")
	@Size(max=15, message="maximo 15 caracteres")
	private String codigo;
	
	@Column(name = "descripcion", length = 50)
	@Size(max=50, message="maximo 50 caracteres")
	private String descripcion;	
	
	@Column(name = "estado", length = 1)
	@Size(max=1, message="maximo 1 caracteres")
	private String estado;
	
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

	public Integer getPatologiaProcedimientoId() {
		return patologiaProcedimientoId;
	}

	public void setPatologiaProcedimientoId(Integer patologiaProcedimientoId) {
		this.patologiaProcedimientoId = patologiaProcedimientoId;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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
