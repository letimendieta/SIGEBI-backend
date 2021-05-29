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
@Table(name = "medicamentos")
public class Medicamentos {
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_medicamentos")
	@SequenceGenerator(name="seq_medicamentos",sequenceName="seq_medicamentos",allocationSize=1)
	private Integer medicamentoId;
	
	@Column(name = "codigo", length = 15, unique = true)
	@Size(max=15, message="maximo 15 caracteres")
	@NotEmpty(message ="no puede estar vacio")
	private String codigo;
	
	@Column(name = "medicamento", length = 100)
	@Size(max=100, message="maximo 100 caracteres")
	private String medicamento;
	
	@Column(name = "concentracion", length = 100)
	@Size(max=100, message="maximo 100 caracteres")
	private String concentracion;
	
	@Column(name = "forma", length = 20)
	@Size(max=20, message="maximo 20 caracteres")
	private String forma;
	
	@Column(name = "via_admin", length = 10)
	@Size(max=10, message="maximo 10 caracteres")
	private String viaAdmin;
	
	@Column(name = "presentacion", length = 50)
	@Size(max=50, message="maximo 50 caracteres")
	private String presentacion;
	
	@Column(name = "clasif_ATQ", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String clasifATQ;	
	
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

	public Integer getMedicamentoId() {
		return medicamentoId;
	}

	public void setMedicamentoId(Integer medicamentoId) {
		this.medicamentoId = medicamentoId;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	public String getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public String getViaAdmin() {
		return viaAdmin;
	}

	public void setViaAdmin(String viaAdmin) {
		this.viaAdmin = viaAdmin;
	}

	public String getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}

	public String getClasifATQ() {
		return clasifATQ;
	}

	public void setClasifATQ(String clasifATQ) {
		this.clasifATQ = clasifATQ;
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
