package com.sigebi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "insumos")
public class Insumos {
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_insumos")
	@SequenceGenerator(name="seq_insumos",sequenceName="seq_insumos",allocationSize=1)
	private Integer insumoId;
	
	@Column(name = "codigo", length = 15, unique = true)
	@Size(max=15, message="maximo 15 caracteres")
	@NotEmpty(message ="no puede estar vacio")
	private String codigo;
	
	@Column(name = "descripcion", length = 30)
	@Size(max=30, message="maximo 30 caracteres")
	private String descripcion;
	
	@Column(name = "fecha_vencimiento")
	private LocalDate fechaVencimiento;
	
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
	
	@OneToOne(mappedBy = "insumos")
    private Stock stock;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	

	public Integer getInsumoId() {
		return insumoId;
	}

	public void setInsumoId(Integer insumoId) {
		this.insumoId = insumoId;
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

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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
