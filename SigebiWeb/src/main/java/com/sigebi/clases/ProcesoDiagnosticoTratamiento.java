package com.sigebi.clases;

import java.util.ArrayList;

import com.sigebi.entity.Anamnesis;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.Diagnosticos;
import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Tratamientos;
import com.sigebi.entity.TratamientosInsumos;

public class ProcesoDiagnosticoTratamiento {
	
	Anamnesis anamnesis;
	Diagnosticos diagnostico;
	Tratamientos tratamiento;
	ArrayList<TratamientosInsumos> tratamientoInsumoList;	
	Consultas consulta;
	ArrayList<FichaMedica> fichaMedicaList;
	HistorialClinico historialClinico;
	
	public Anamnesis getAnamnesis() {
		return anamnesis;
	}
	public void setAnamnesis(Anamnesis anamnesis) {
		this.anamnesis = anamnesis;
	}
	public Diagnosticos getDiagnostico() {
		return diagnostico;
	}
	public void setDiagnostico(Diagnosticos diagnostico) {
		this.diagnostico = diagnostico;
	}
	public Tratamientos getTratamiento() {
		return tratamiento;
	}
	public void setTratamiento(Tratamientos tratamiento) {
		this.tratamiento = tratamiento;
	}

	public ArrayList<TratamientosInsumos> getTratamientoInsumoList() {
		return tratamientoInsumoList;
	}
	public void setTratamientoInsumoList(ArrayList<TratamientosInsumos> tratamientoInsumoList) {
		this.tratamientoInsumoList = tratamientoInsumoList;
	}
	public Consultas getConsulta() {
		return consulta;
	}
	public void setConsulta(Consultas consulta) {
		this.consulta = consulta;
	}
	public ArrayList<FichaMedica> getFichaMedicaList() {
		return fichaMedicaList;
	}
	public void setFichaMedicaList(ArrayList<FichaMedica> fichaMedicaList) {
		this.fichaMedicaList = fichaMedicaList;
	}
	public HistorialClinico getHistorialClinico() {
		return historialClinico;
	}
	public void setHistorialClinico(HistorialClinico historialClinico) {
		this.historialClinico = historialClinico;
	}
}
