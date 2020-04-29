package com.sigebi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stock {
	
	@Id
	private int stockId;
	
	@Column(name = "insumo_id")
	private Integer insumoId;
	
	@Column(name = "cantidad")
	private Integer cantidad;
	
	@Column(name = "unidad_medida", length = 10)
	private String unidad_Medida;

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
	
	
}
