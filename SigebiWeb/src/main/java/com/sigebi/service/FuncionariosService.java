package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Funcionarios;

public interface FuncionariosService {

	public List<Funcionarios> findAll();
	
	public Funcionarios findById(int id);
	
	public int count();
	
	public Funcionarios guardar(Funcionarios funcionario) throws Exception;
	
	public Funcionarios actualizar(Funcionarios funcionario) throws Exception;	
	
	public void delete(int id);
	
	public List<Funcionarios> buscar(Date fromDate, Date toDate, Funcionarios funcionarios, List<Integer> personasId, String orderBy, String orderDir, Pageable pageable);
	
	public List<Funcionarios> buscarNoPaginable(Date fromDate, Date toDate, Funcionarios funcionario, List<Integer> personasId);
}
