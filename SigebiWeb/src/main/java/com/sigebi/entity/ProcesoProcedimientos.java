package com.sigebi.entity;

import java.util.ArrayList;

public class ProcesoProcedimientos {
		
	Procedimientos procedimiento;
	ArrayList<ProcedimientosInsumos> procedimientoInsumoList;
	
	public Procedimientos getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(Procedimientos procedimiento) {
		this.procedimiento = procedimiento;
	}
	public ArrayList<ProcedimientosInsumos> getProcedimientoInsumoList() {
		return procedimientoInsumoList;
	}
	public void setProcedimientoInsumoList(ArrayList<ProcedimientosInsumos> procedimientoInsumoList) {
		this.procedimientoInsumoList = procedimientoInsumoList;
	}	
}
