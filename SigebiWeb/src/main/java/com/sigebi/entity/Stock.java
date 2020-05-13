package com.sigebi.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int stockId;
	
	@Column(name = "insumo_id")
	private Integer insumoId;
	
	@Column(name = "cantidad")
	private Integer cantidad;
	
	@Column(name = "unidad_medida", length = 10)
	private String unidad_Medida;
	
	@Column(name = "fecha_creacion")
	private Timestamp fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private Timestamp fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	private String usuarioModificacion;

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public Integer getInsumoId() {
		return insumoId;
	}

	public void setInsumoId(Integer insumoId) {
		this.insumoId = insumoId;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidad_Medida() {
		return unidad_Medida;
	}

	public void setUnidad_Medida(String unidad_Medida) {
		this.unidad_Medida = unidad_Medida;
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
