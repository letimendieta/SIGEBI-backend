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
public class Consultas {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_consultas")
	@SequenceGenerator(name="seq_consultas",sequenceName="seq_consultas",allocationSize=1)
	private Integer consultaId;
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
		
	@Column(name = "paciente_id")
	private Integer pacienteId;
	
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
	
	@OneToOne
    @JoinColumn(name = "area_id", referencedColumnName = "areaId")
    private Areas areas;
	
	@OneToOne
    @JoinColumn(name = "diagnostico_id", referencedColumnName = "diagnosticoId")
    private Diagnosticos diagnosticos;
	
	@OneToOne
    @JoinColumn(name = "tratamiento_id", referencedColumnName = "tratamientoId")
    private Tratamientos tratamientos;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}
	
	public Integer getConsultaId() {
		return consultaId;
	}

	public void setConsultaId(Integer datoConsultaId) {
		this.consultaId = datoConsultaId;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Integer getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public Diagnosticos getDiagnosticos() {
		return diagnosticos;
	}

	public void setDiagnosticos(Diagnosticos diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	public Tratamientos getTratamientos() {
		return tratamientos;
	}

	public void setTratamientos(Tratamientos tratamientos) {
		this.tratamientos = tratamientos;
	}

	public Areas getAreas() {
		return areas;
	}

	public void setAreas(Areas areas) {
		this.areas = areas;
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
