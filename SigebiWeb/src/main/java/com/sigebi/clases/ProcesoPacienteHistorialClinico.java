package com.sigebi.clases;

import java.util.ArrayList;
import java.util.List;

import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Preguntas;

public class ProcesoPacienteHistorialClinico {

	Pacientes paciente;
	HistorialClinico historialClinico;
	List<Integer> alergenosIdList = new ArrayList<Integer>();
	List<Integer> patologiasProcedimientosIdList = new ArrayList<Integer>();
	List<Integer> patologiasFamiliaresIdList = new ArrayList<Integer>();
	List<Integer> vacunasIdList = new ArrayList<Integer>();
	List<Preguntas> preguntasList = new ArrayList<Preguntas>();
	
	public Pacientes getPaciente() {
		return paciente;
	}
	public void setPaciente(Pacientes paciente) {
		this.paciente = paciente;
	}	
	public HistorialClinico getHistorialClinico() {
		return historialClinico;
	}
	public void setHistorialClinico(HistorialClinico historialClinico) {
		this.historialClinico = historialClinico;
	}
	public List<Integer> getAlergenosIdList() {
		return alergenosIdList;
	}
	public void setAlergenosIdList(List<Integer> alergenosIdList) {
		this.alergenosIdList = alergenosIdList;
	}
	public List<Integer> getPatologiasProcedimientosIdList() {
		return patologiasProcedimientosIdList;
	}
	public void setPatologiasProcedimientosIdList(List<Integer> patologiasProcedimientosIdList) {
		this.patologiasProcedimientosIdList = patologiasProcedimientosIdList;
	}
	public List<Integer> getPatologiasFamiliaresIdList() {
		return patologiasFamiliaresIdList;
	}
	public void setPatologiasFamiliaresIdList(List<Integer> patologiasFamiliaresIdList) {
		this.patologiasFamiliaresIdList = patologiasFamiliaresIdList;
	}
	public List<Integer> getVacunasIdList() {
		return vacunasIdList;
	}
	public void setVacunasIdList(List<Integer> vacunasIdList) {
		this.vacunasIdList = vacunasIdList;
	}
	public List<Preguntas> getPreguntasList() {
		return preguntasList;
	}
	public void setPreguntasList(List<Preguntas> preguntasList) {
		this.preguntasList = preguntasList;
	}
	
}
