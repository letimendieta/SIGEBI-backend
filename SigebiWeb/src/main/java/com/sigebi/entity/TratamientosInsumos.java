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

@Entity
@Table(name = "tratamientos_insumos")
public class TratamientosInsumos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_tratamientos_insumos")
	@SequenceGenerator(name="seq_tratamientos_insumos",sequenceName="seq_tratamientos_insumos",allocationSize=1)
	private Integer tratamientoInsumoId;	
	
	@Column(name = "cantidad")
	private Integer cantidad;	
		
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	private String usuarioModificacion;
	
	@OneToOne
    @JoinColumn(name = "tratamiento_id", referencedColumnName = "tratamientoId")
    private Tratamientos tratamientos;
	
	@OneToOne
    @JoinColumn(name = "insumo_id", referencedColumnName = "insumoId")
    private Insumos insumos;	
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	
	
	public Integer getTratamientoInsumoId() {
		return tratamientoInsumoId;
	}

	public void setTratamientoInsumoId(Integer tratamientoInsumoId) {
		this.tratamientoInsumoId = tratamientoInsumoId;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Tratamientos getTratamientos() {
		return tratamientos;
	}

	public void setTratamientos(Tratamientos tratamientos) {
		this.tratamientos = tratamientos;
	}

	public Insumos getInsumos() {
		return insumos;
	}

	public void setInsumos(Insumos insumos) {
		this.insumos = insumos;
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
