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
@Table(name = "vacunaciones")
public class Vacunaciones {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_vacunaciones")
	@SequenceGenerator(name="seq_vacunaciones",sequenceName="seq_vacunaciones",allocationSize=1)
	private Integer vacunacionId;
	
	@Column(name = "historial_clinico_id")
	private Integer historialClinicoId;
		
	@Column(name = "fecha_creacion")
	private LocalDateTime fechaCreacion;	
	
	@Column(name = "usuario_creacion", length = 15)
	private String usuarioCreacion;
	
	@Column(name = "fecha_modificacion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "usuario_modificacion", length = 15)
	private String usuarioModificacion;
	
	@OneToOne
    @JoinColumn(name = "vacuna_id", referencedColumnName = "vacunaId")
    private Vacunas vacunas;		
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}

	public Integer getVacunacionId() {
		return vacunacionId;
	}

	public void setVacunacionId(Integer vacunacionId) {
		this.vacunacionId = vacunacionId;
	}

	public Integer getHistorialClinicoId() {
		return historialClinicoId;
	}

	public void setHistorialClinicoId(Integer historialClinicoId) {
		this.historialClinicoId = historialClinicoId;
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
