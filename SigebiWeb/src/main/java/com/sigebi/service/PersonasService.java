package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Personas;
import com.sigebi.util.exceptions.SigebiException;

public interface PersonasService {
	
	public List<Personas> listar() throws SigebiException ;
	
	public Personas obtener(int id) throws SigebiException;
	
	public Personas guardar(Personas persona) throws SigebiException;
	
	public Personas actualizar(Personas persona) throws SigebiException;
	
	public void eliminar(int id) throws SigebiException;
	
	public List<Personas> buscar(Date fromDate, Date toDate, Personas persona, Pageable pageable) throws DataAccessException;
	
	
}
