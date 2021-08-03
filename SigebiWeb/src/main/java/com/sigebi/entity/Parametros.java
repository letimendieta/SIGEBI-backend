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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Parametros {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_parametros")
	@SequenceGenerator(name="seq_parametros",sequenceName="seq_parametros",allocationSize=1)
	private Integer parametroId;
	
	@Column(name = "codigo_parametro", length = 30)
	@NotEmpty(message ="no puede estar vacio")
	@Size(max=30, message="maximo 30 caracteres")
	private String codigoParametro;
	
	@Column(name = "descripcion", length = 100)
	@Size(max=100, message="maximo 100 caracteres")
	private String descripcion;
	
	@Column(name = "nombre", length = 20)
	@Size(max=20, message="maximo 20 caracteres")
	private String nombre;
	
	@Column(name = "valor", length = 100)
	@Size(max=100, message="maximo 100 caracteres")
	private String valor;
	
	@Column(name = "descripcion_valor", length = 30)
	@Size(max=30, message="maximo 30 caracteres")
	private String descripcionValor;
	
	@Column(name = "estado", length = 1)
	@Size(max=1, message="maximo 1 caracteres")
	private String estado;
	
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
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	

	public Integer getParametroId() {
		return parametroId;
	}

	public void setParametroId(Integer parametroId) {
		this.parametroId = parametroId;
	}

	public String getCodigoParametro() {
		return codigoParametro;
	}

	public void setCodigoParametro(String codigoParametro) {
		this.codigoParametro = codigoParametro;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
		
	public String getDescripcionValor() {
		return descripcionValor;
	}

	public void setDescripcionValor(String descripcionValor) {
		this.descripcionValor = descripcionValor;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
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
