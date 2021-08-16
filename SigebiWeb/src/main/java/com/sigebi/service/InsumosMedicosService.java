package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.InsumosMedicos;
import com.sigebi.util.exceptions.SigebiException;

public interface InsumosMedicosService {
	
	public List<InsumosMedicos> listar();
		
	public InsumosMedicos obtener(int id);
	
	public int count();
		
	public InsumosMedicos guardar(InsumosMedicos insumoMedico);
	
	public InsumosMedicos actualizar(InsumosMedicos insumoMedico) throws SigebiException;
		
	public void eliminar(int id);
		
	public List<InsumosMedicos> buscar(Date fromDate, Date toDate, InsumosMedicos insumo, String orderBy, String orderDir, Pageable pageable);
		
	public List<InsumosMedicos> buscarNoPaginable(Date fromDate, Date toDate, InsumosMedicos insumo);
}
