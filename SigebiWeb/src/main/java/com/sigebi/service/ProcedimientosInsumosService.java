package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Procedimientos;
import com.sigebi.entity.ProcedimientosInsumos;

public interface ProcedimientosInsumosService {
	
	public List<ProcedimientosInsumos> listar();
	
	public int count();
	
	public ProcedimientosInsumos obtener(int id);
	
	public List<ProcedimientosInsumos> obtenerProcedimientoInsumoPaciente(int pacienteId);
	
	public ProcedimientosInsumos save(ProcedimientosInsumos procedimientoInsumo);
	
	public void delete(int id);
	
	public List<ProcedimientosInsumos> buscar(Date fromDate, Date toDate, ProcedimientosInsumos procedimientoInsumo, String orderBy, String orderDir, Pageable pageable);
	
	public List<ProcedimientosInsumos> buscarNoPaginable(Date fromDate, Date toDate, ProcedimientosInsumos procedimientoInsumo, List<Integer> procedimientosId);
	
}
