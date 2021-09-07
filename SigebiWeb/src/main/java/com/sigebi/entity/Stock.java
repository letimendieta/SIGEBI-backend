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
import javax.validation.constraints.Size;

@Entity
public class Stock {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_stock")
	@SequenceGenerator(name="seq_stock",sequenceName="seq_stock",allocationSize=1)
	private Integer stockId;
		
	@Column(name = "cantidad")
	private Integer cantidad;
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;
	
	@Column(name = "notas", length = 100)
	@Size(max=100, message="maximo 100 caracteres")
	private String notas;
	
	@Column(name = "usuario_creacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String usuarioModificacion;
	
	@OneToOne
    @JoinColumn(name = "insumo_medico_id", referencedColumnName = "insumoMedicoId")
    private InsumosMedicos insumosMedicos;
	
	@OneToOne
    @JoinColumn(name = "medicamento_id", referencedColumnName = "medicamentoId")
    private Medicamentos medicamentos;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}	

	public InsumosMedicos getInsumosMedicos() {
		return insumosMedicos;
	}

	public void setInsumosMedicos(InsumosMedicos insumosMedicos) {
		this.insumosMedicos = insumosMedicos;
	}

	public Medicamentos getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(Medicamentos medicamentos) {
		this.medicamentos = medicamentos;
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

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}
		
}
