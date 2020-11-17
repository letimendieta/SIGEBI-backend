package com.sigebi.entity;

import java.util.ArrayList;

public class ProcesoDiagnosticoTratamiento {
		
	Diagnosticos diagnostico;
	Tratamientos tratamiento;
	ArrayList<TratamientosInsumos> tratamientoInsumoList;	
	Consultas consulta;
	
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
}
