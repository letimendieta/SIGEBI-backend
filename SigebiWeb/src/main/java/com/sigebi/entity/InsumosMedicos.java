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
@Table(name = "insumos_medicos")
public class InsumosMedicos {
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_insumos_medicos")
	@SequenceGenerator(name="seq_insumos_medicos",sequenceName="seq_insumos_medicos",allocationSize=1)
	private Integer insumoMedicoId;
	
	@Column(name = "codigo", length = 15, unique = true)
	@Size(max=15, message="maximo 15 caracteres")
	@NotEmpty(message ="no puede estar vacio")
	private String codigo;
	
	@Column(name = "nombre", length = 150)
	@Size(max=150, message="maximo 150 caracteres")
	private String nombre;
	
	@Column(name = "caracteristicas", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String caracteristicas;
	
	@Column(name = "compatible", length = 150)
	@Size(max=150, message="maximo 150 caracteres")
	private String compatible;
	
	@Column(name = "presentacion", length = 150)
	@Size(max=150, message="maximo 150 caracteres")
	private String presentacion;
	
	@Column(name = "unidad_medida", length = 20)
	@Size(max=20, message="maximo 20 caracteres")
	private String unidadMedida;
	
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

	public Integer getInsumoMedicoId() {
		return insumoMedicoId;
	}

	public void setInsumoMedicoId(Integer insumoMedicoId) {
		this.insumoMedicoId = insumoMedicoId;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public String getCompatible() {
		return compatible;
	}

	public void setCompatible(String compatible) {
		this.compatible = compatible;
	}

	public String getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
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
