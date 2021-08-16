package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Funcionarios;
import com.sigebi.security.entity.Usuario;
import com.sigebi.util.exceptions.SigebiException;


public interface UsuariosService {

	public int count();
	
	public Usuario findById(int id);	
	
	public Usuario findByFuncionario(Funcionarios funcionario);
	
	public String generarNombreUsuario(Integer personaId) throws SigebiException;
		
	public List<Usuario> buscar(Date fromDate, Date toDate, Usuario usuario, List<Integer> funcionariosId, String orderBy, String orderDir, Pageable pageable);
		
}
