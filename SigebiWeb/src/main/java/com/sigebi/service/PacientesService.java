package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import com.sigebi.clases.ProcesoPacienteHistorialClinico;
import com.sigebi.entity.HistorialClinico;
import com.sigebi.entity.Pacientes;
import com.sigebi.util.exceptions.SigebiException;

public interface PacientesService{

	public List<Pacientes> listar() throws SigebiException;
	
	public Pacientes obtener(int id) throws SigebiException;
	
	public int count();
		
	public Pacientes guardar(Pacientes paciente) throws SigebiException;
	
	public Pacientes guardarPacienteHistorialClinico(ProcesoPacienteHistorialClinico paciente) throws SigebiException;
	
	public Pacientes actualizarPacienteHistorialClinico(ProcesoPacienteHistorialClinico paciente) throws SigebiException;
	
	public Pacientes actualizar(Pacientes paciente) throws SigebiException;
	
	public void eliminar(int id) throws SigebiException;
	
	public List<Pacientes> buscar(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId, String orderBy, String orderDir, Pageable pageable);
	
	public List<Pacientes> buscarNoPaginable(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId) throws DataAccessException;
	
	public List<Pacientes> buscarPacientes(Date fromDate, Date toDate, Pacientes paciente, String orderBy, String orderDir, Pageable pageable);
}
