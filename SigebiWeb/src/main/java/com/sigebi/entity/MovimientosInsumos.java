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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "movimientos_insumos")
public class MovimientosInsumos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_movimientos_insumos")
	@SequenceGenerator(name="seq_movimientos_insumos",sequenceName="seq_movimientos_insumos",allocationSize=1)
	private Integer movimientoInsumoId;
	
	@Column(name = "cantidad_entrada")
	private Integer cantidadEntrada;
	
	@Column(name = "cantidad_salida")
	private Integer cantidadSalida;
	
	@Column(name = "cod_proceso", length = 15)
	private String codProceso;
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;	
	
	@OneToOne
    @JoinColumn(name = "insumo_id", referencedColumnName = "insumoId")
    private Insumos insumos;	
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}


	public Integer getMovimientoInsumoId() {
		return movimientoInsumoId;
	}


	public void setMovimientoInsumoId(Integer movimientoInsumoId) {
		this.movimientoInsumoId = movimientoInsumoId;
	}


	public Integer getCantidadEntrada() {
		return cantidadEntrada;
	}


	public void setCantidadEntrada(Integer cantidadEntrada) {
		this.cantidadEntrada = cantidadEntrada;
	}


	public Integer getCantidadSalida() {
		return cantidadSalida;
	}


	public void setCantidadSalida(Integer cantidadSalida) {
		this.cantidadSalida = cantidadSalida;
	}


	public String getCodProceso() {
		return codProceso;
	}


	public void setCodProceso(String codProceso) {
		this.codProceso = codProceso;
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


	public Insumos getInsumos() {
		return insumos;
	}


	public void setInsumos(Insumos insumos) {
		this.insumos = insumos;
	}
	
}
