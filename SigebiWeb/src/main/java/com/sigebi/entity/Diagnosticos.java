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
import javax.validation.constraints.Size;

@Entity
@Table(name = "diagnosticos")
public class Diagnosticos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_diagnosticos")
	@SequenceGenerator(name="seq_diagnosticos",sequenceName="seq_diagnosticos",allocationSize=1)
	private Integer diagnosticoId;
	
	@Column(name = "diagnostico_principal", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String diagnosticoPrincipal;
	
	@Column(name = "diagnostico_secundario", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String diagnosticoSecundario;
	
	@Column(name = "enfermedad_cie10_primaria_id")
	private Integer enfermedadCie10PrimariaId;
	
	@Column(name = "enfermedad_cie10_secundaria_id")
	private Integer enfermedadCie10SecundariaId;
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	private String usuarioModificacion;
		
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	

	public Integer getDiagnosticoId() {
		return diagnosticoId;
	}

	public void setDiagnosticoId(Integer diagnosticoId) {
		this.diagnosticoId = diagnosticoId;
	}

	public String getDiagnosticoPrincipal() {
		return diagnosticoPrincipal;
	}

	public void setDiagnosticoPrincipal(String diagnosticoPrincipal) {
		this.diagnosticoPrincipal = diagnosticoPrincipal;
	}

	public String getDiagnosticoSecundario() {
		return diagnosticoSecundario;
	}

	public void setDiagnosticoSecundario(String diagnosticoSecundario) {
		this.diagnosticoSecundario = diagnosticoSecundario;
	}	

	public Integer getEnfermedadCie10PrimariaId() {
		return enfermedadCie10PrimariaId;
	}

	public void setEnfermedadCie10PrimariaId(Integer enfermedadCie10PrimariaId) {
		this.enfermedadCie10PrimariaId = enfermedadCie10PrimariaId;
	}

	public Integer getEnfermedadCie10SecundariaId() {
		return enfermedadCie10SecundariaId;
	}

	public void setEnfermedadCie10SecundariaId(Integer enfermedadCie10SecundariaId) {
		this.enfermedadCie10SecundariaId = enfermedadCie10SecundariaId;
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
