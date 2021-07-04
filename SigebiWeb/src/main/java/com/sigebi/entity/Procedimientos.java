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
public class Procedimientos {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_procedimientos")
	@SequenceGenerator(name="seq_procedimientos",sequenceName="seq_procedimientos",allocationSize=1)
	private Integer procedimientoId;
	
	@Column(name = "notas", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String notas;
				
	@Column(name = "cantidad_insumo")
	private Integer cantidadInsumo;
	
	@Column(name = "estado", length = 10)
	@Size(max=10, message="maximo 10 caracteres")
	private String estado;
	
	@OneToOne
    @JoinColumn(name = "area_id", referencedColumnName = "areaId")
    private Areas areas;
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
	
	@Column(name = "consulta_id")
	private Integer consultaId;
		
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
    @JoinColumn(name = "funcionario_id", referencedColumnName = "funcionarioId")
    private Funcionarios funcionarios;
	
	@OneToOne
    @JoinColumn(name = "paciente_id", referencedColumnName = "pacienteId")
    private Pacientes pacientes;
	
	@OneToOne
    @JoinColumn(name = "motivo_consulta_id", referencedColumnName = "motivoConsultaId", updatable = true)
    private MotivosConsulta motivoConsulta;
	
	@OneToOne
    @JoinColumn(name = "cita_id", referencedColumnName = "citaId", updatable = true)
    private Citas cita;
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}

	public Integer getProcedimientoId() {
		return procedimientoId;
	}

	public void setProcedimientoId(Integer procedimientoId) {
		this.procedimientoId = procedimientoId;
	}

	public Funcionarios getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}

	public Areas getAreas() {
		return areas;
	}

	public void setAreas(Areas areas) {
		this.areas = areas;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

	public Pacientes getPacientes() {
		return pacientes;
	}

	public void setPacientes(Pacientes pacientes) {
		this.pacientes = pacientes;
	}

	public Integer getCantidadInsumo() {
		return cantidadInsumo;
	}

	public void setCantidadInsumo(Integer cantidadInsumo) {
		this.cantidadInsumo = cantidadInsumo;
	}

	public MotivosConsulta getMotivoConsulta() {
		return motivoConsulta;
	}

	public Citas getCita() {
		return cita;
	}

	public void setCita(Citas cita) {
		this.cita = cita;
	}

	public void setMotivoConsulta(MotivosConsulta motivoConsulta) {
		this.motivoConsulta = motivoConsulta;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Integer getConsultaId() {
		return consultaId;
	}

	public void setConsultaId(Integer consultaId) {
		this.consultaId = consultaId;
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
	
	
}
