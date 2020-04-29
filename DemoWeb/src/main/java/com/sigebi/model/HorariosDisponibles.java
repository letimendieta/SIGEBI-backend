package com.sigebi.model;

import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HorariosDisponibles {	
	
	@Id
	private int horarioDisponibleId;
	
	@Column(name = "funcionario_id")
	private Integer funcionarioId;
	
	@Column(name = "fecha")
	private Timestamp fecha;
	
	@Column(name = "hora_inicio")
	private Time horaInicio;
	
	@Column(name = "hora_fin")
	private Time horaFin;
		
	@Column(name = "estado", length = 1)
	private String estado;

	public int getHorarioDisponibleId() {
		return horarioDisponibleId;
	}

	public void setHorarioDisponibleId(int horarioDisponibleId) {
		this.horarioDisponibleId = horarioDisponibleId;
	}

	public Integer getFuncionarioId() {
		return funcionarioId;
	}

	public void setFuncionarioId(Integer funcionarioId) {
		this.funcionarioId = funcionarioId;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Time getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Time getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
}
