package com.sigebi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
public class HorariosDisponibles {	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_horarios_disponibles")
	@SequenceGenerator(name="seq_horarios_disponibles",sequenceName="seq_horarios_disponibles",allocationSize=1)
	private Integer horarioDisponibleId;
		
	@Column(name = "fecha")
	private LocalDate fecha;
	
	@Column(name = "hora_inicio")
	private LocalTime horaInicio;
	
	@Column(name = "hora_fin")
	private LocalTime horaFin;
		
	@Column(name = "estado", length = 1)
	@Size(max=1, message="maximo 1 caracteres")
	private String estado;
	
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
    @JoinColumn(name = "funcionario_id", referencedColumnName = "funcionarioId", unique = true)
    private Funcionarios funcionarios;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}
	
	public Integer getHorarioDisponibleId() {
		return horarioDisponibleId;
	}

	public void setHorarioDisponibleId(Integer horarioDisponibleId) {
		this.horarioDisponibleId = horarioDisponibleId;
	}
		
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Funcionarios getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
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
	
	
}
