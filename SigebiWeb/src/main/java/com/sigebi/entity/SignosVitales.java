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
public class SignosVitales {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_signos_vitales")
	@SequenceGenerator(name="seq_signos_vitales",sequenceName="seq_signos_vitales",allocationSize=1)
	private Integer signoVitalId;
	
	@Column(name = "pulso")
	private Integer pulso;
	
	@Column(name = "frecuencia_cardiaca")
	private Integer frecuenciaCardiaca;
	
	@Column(name = "frecuencia_respiratoria")
	private Integer frecuenciaRespiratoria;
	
	@Column(name = "presion_sistolica")
	private Integer presionSistolica;
	
	@Column(name = "presion_diastolica")
	private Integer presionDiastolica;
	
	@Column(name = "temperatura")
	private Double temperatura;
	
	@Column(name = "peso")
	private Double peso;
	
	@Column(name = "talla")
	private Double talla;
	
	@Column(name = "indice_masa_corporal")
	private Double indiceMasaCorporal;
	
	@Column(name = "cia_abdominal")
	private Double ciaAbdominal;
	
	@Column(name = "notas", length = 500)
	@Size(max=500, message="maximo 500 caracteres")
	private String notas;
	
	@Column(name = "fecha")
	private LocalDateTime fecha;
		
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
	
	@PrePersist
	private void create() {
		this.fechaCreacion = LocalDateTime.now();
	}
	
	@PreUpdate
	private void update() {
		this.fechaModificacion = LocalDateTime.now();
	}
		
	public Integer getSignoVitalId() {
		return signoVitalId;
	}

	public void setSignoVitalId(Integer signoVitalId) {
		this.signoVitalId = signoVitalId;
	}

	public Integer getPulso() {
		return pulso;
	}

	public void setPulso(Integer pulso) {
		this.pulso = pulso;
	}

	public Integer getFrecuenciaCardiaca() {
		return frecuenciaCardiaca;
	}

	public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
		this.frecuenciaCardiaca = frecuenciaCardiaca;
	}

	public Integer getFrecuenciaRespiratoria() {
		return frecuenciaRespiratoria;
	}

	public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
		this.frecuenciaRespiratoria = frecuenciaRespiratoria;
	}

	public Integer getPresionSistolica() {
		return presionSistolica;
	}

	public void setPresionSistolica(Integer presionSistolica) {
		this.presionSistolica = presionSistolica;
	}

	public Integer getPresionDiastolica() {
		return presionDiastolica;
	}

	public void setPresionDiastolica(Integer presionDiastolica) {
		this.presionDiastolica = presionDiastolica;
	}

	public Double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Double getTalla() {
		return talla;
	}

	public void setTalla(Double talla) {
		this.talla = talla;
	}

	public Double getCiaAbdominal() {
		return ciaAbdominal;
	}

	public void setCiaAbdominal(Double ciaAbdominal) {
		this.ciaAbdominal = ciaAbdominal;
	}

	public Double getIndiceMasaCorporal() {
		return indiceMasaCorporal;
	}

	public void setIndiceMasaCorporal(Double indiceMasaCorporal) {
		this.indiceMasaCorporal = indiceMasaCorporal;
	}

	public Funcionarios getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
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

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
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
