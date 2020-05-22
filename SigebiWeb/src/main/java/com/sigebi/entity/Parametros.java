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
public class Parametros {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer parametroId;
	
	@Column(name = "codigo_parametro", length = 20, unique = true)
	@NotEmpty(message ="no puede estar vacio")
	private String codigoParametro;
	
	@Column(name = "nombre", length = 20)
	@Size(max=20, message="maximo 20 caracteres")
	private String nombre;
	
	@Column(name = "descripcion", length = 30)
	@Size(max=30, message="maximo 30 caracteres")
	private String descripcion;
	
	@Column(name = "valor", length = 20)
	@Size(max=20, message="maximo 20 caracteres")
	private String valor;
	
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
