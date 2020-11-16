package com.sigebi.entity;

import java.util.List;

public class ProcesoDiagnosticoTratamiento {
		
	Diagnosticos diagnostico;
	Tratamientos tratamiento;
	List<TratamientosInsumos> tratamientoInsumo;	
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
	public List<TratamientosInsumos> getTratamientoInsumo() {
		return tratamientoInsumo;
	}
	public void setTratamientoInsumo(List<TratamientosInsumos> tratamientoInsumo) {
		this.tratamientoInsumo = tratamientoInsumo;
	}
	public Consultas getConsulta() {
		return consulta;
	}
	public void setConsulta(Consultas consulta) {
		this.consulta = consulta;
	}		
}
