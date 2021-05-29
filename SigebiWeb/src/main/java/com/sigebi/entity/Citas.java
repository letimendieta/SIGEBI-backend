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
public class Citas {
		
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_citas")
	@SequenceGenerator(name="seq_citas",sequenceName="seq_citas",allocationSize=1)
	private Integer citaId;
	
	@Column(name = "fecha")
	private LocalDate fecha;
		
	@Column(name = "hora")
	private LocalTime hora;
		
	@Column(name = "estado", length = 15)
	@Size(max=15, message="maximo 15 caracteres")
	private String estado;
	
	@Column(name = "notas", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String notas;
	
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
	
	@OneToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "pacienteId", unique = true)
    private Pacientes pacientes;
	
	@OneToOne
    @JoinColumn(name = "area_id", referencedColumnName = "areaId", unique = true)
    private Areas areas;
	
	@OneToOne
    @JoinColumn(name = "motivo_consulta_id", referencedColumnName = "motivoConsultaId", updatable = true)
    private MotivosConsulta motivoConsulta;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}

	public Integer getCitaId() {
		return citaId;
	}

	public void setCitaId(Integer citaId) {
		this.citaId = citaId;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public Areas getAreas() {
		return areas;
	}

	public void setAreas(Areas areas) {
		this.areas = areas;
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

	public Pacientes getPacientes() {
		return pacientes;
	}

	public void setPacientes(Pacientes pacientes) {
		this.pacientes = pacientes;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

	public MotivosConsulta getMotivoConsulta() {
		return motivoConsulta;
	}

	public void setMotivoConsulta(MotivosConsulta motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
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
