package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Medicamentos;
import com.sigebi.util.exceptions.SigebiException;

public interface MedicamentosService {
	
	public List<Medicamentos> listar();
	
	public Medicamentos obtener(int id);
	
	public int count();
				
	public Medicamentos guardar(Medicamentos medicamento);
	
	public Medicamentos actualizar(Medicamentos medicamento) throws SigebiException;
	
	public void eliminar(int id);
			
	public List<Medicamentos> buscar(Date fromDate, Date toDate, Medicamentos medicamento, String orderBy, String orderDir, Pageable pageable);
	
	public List<Medicamentos> buscarNoPaginable(Date fromDate, Date toDate, Medicamentos medicamento);
	
}
