package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import com.sigebi.clases.ConsultasResult;
import com.sigebi.entity.Consultas;
import com.sigebi.util.exceptions.SigebiException;

public interface ConsultasService {
	
	public List<Consultas> listar() throws SigebiException;
	
	public int count();
	
	public Consultas obtener(int id) throws SigebiException;
	
	public Consultas guardar(Consultas consulta) throws SigebiException;
	
	public Consultas actualizar(Consultas consulta) throws SigebiException;
	
	public void eliminar(int id) throws SigebiException;
	
	public List<Consultas> buscar(Date fromDate, Date toDate, Consultas consulta, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException;
	
	public List<ConsultasResult> buscarConsultas(Date fromDate, Date toDate, Consultas consulta, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException;
	
	
}
