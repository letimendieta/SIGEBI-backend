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
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "antecedentes")
public class Antecedentes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_antecedentes")
	@SequenceGenerator(name="seq_antecedentes",sequenceName="seq_antecedentes",allocationSize=1)
	private Integer antecedenteId;
	
	@Column(name = "paciente_id")
	private Integer pacienteId;
	
	@Column(name = "tipo", length = 20, nullable = false)
	@NotEmpty(message ="no puede estar vacio")
	private String tipo;
	
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	private String usuarioModificacion;
	
	@OneToOne
    @JoinColumn(name = "patologia_procedimiento_id", referencedColumnName = "patologiaProcedimientoId")
    private PatologiasProcedimientos patologiasProcedimientos;		
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}	
	
	public Integer getAntecedenteId() {
		return antecedenteId;
	}

	public void setAntecedenteId(Integer antecedenteId) {
		this.antecedenteId = antecedenteId;
	}
	
	public Integer getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public PatologiasProcedimientos getPatologiasProcedimientos() {
		return patologiasProcedimientos;
	}

	public void setPatologiasProcedimientos(PatologiasProcedimientos patologiasProcedimientos) {
		this.patologiasProcedimientos = patologiasProcedimientos;
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
