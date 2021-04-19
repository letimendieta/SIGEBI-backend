package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Areas;
import com.sigebi.util.exceptions.SigebiException;

public interface AreasService {
	
	public List<Areas> listar() throws SigebiException;
	
	public int count();
	
	public Areas obtener(int id) throws SigebiException;
	
	public Areas guardar(Areas area);
	
	public Areas actualizar(Areas area) throws SigebiException;
	
	public void eliminar(int id) throws SigebiException;
	
	public List<Areas> buscar(Date fromDate, Date toDate, Areas area, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException;
	
	
}
