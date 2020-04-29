package com.sigebi.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Procedimientos {
	
	@Id
	private int procedimiento_id;
	
	@Column(name = "funcionario_id")
	private Integer funcionarioId;
	
	@Column(name = "descripcion", length = 500)
	private String descripcion;
	
	@Column(name = "paciente_id")
	private Integer pacienteId;
	
	@Column(name = "insumo_id")
	private Integer insumoId;
	
	@Column(name = "cantidad_insumo")
	private Integer cantidadInsumo;
	
	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "hora")
	private Time hora;

	public int getProcedimiento_id() {
		return procedimiento_id;
	}

	public void setProcedimiento_id(int procedimiento_id) {
		this.procedimiento_id = procedimiento_id;
	}

	public Integer getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(Integer funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Integer pacienteId) {
		this.pacienteId = pacienteId;
	}

	public Integer getInsumoId() {
		return insumoId;
	}

	public void setInsumoId(Integer insumoId) {
		this.insumoId = insumoId;
	}

	public Integer getCantidadInsumo() {
		return cantidadInsumo;
	}

	public void setCantidadInsumo(Integer cantidadInsumo) {
		this.cantidadInsumo = cantidadInsumo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}
	
	
}
