package com.sigebi.service;

import com.sigebi.clases.ProcesoDiagnosticoTratamiento;
import com.sigebi.entity.Consultas;
import com.sigebi.util.exceptions.SigebiException;

public interface ProcesoDiagnosticoTratamientoService {
		
	public Consultas guardar(ProcesoDiagnosticoTratamiento procesoDiagnosticoTratamiento) throws SigebiException;
	
}
