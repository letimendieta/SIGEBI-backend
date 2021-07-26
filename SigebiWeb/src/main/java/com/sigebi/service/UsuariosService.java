package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Funcionarios;
import com.sigebi.security.entity.Usuario;
import com.sigebi.util.exceptions.SigebiException;


public interface UsuariosService {

	//public List<Usuarios> findAll();
	
	public Usuario findById(int id);	
	
	public Usuario findByFuncionario(Funcionarios funcionario);
	
	public String generarNombreUsuario(Integer personaId) throws SigebiException;
	
	/*public Usuarios actualizar(Usuarios usuario) throws Exception;
	
	public void delete(int id);*/
	
	//public List<Usuarios> buscar(Date fromDate, Date toDate, Usuarios usuario, List<Integer> personasId, Pageable pageable);
	
	public List<Usuario> buscar(Date fromDate, Date toDate, Usuario usuario, List<Integer> funcionariosId, Pageable pageable);
	
	/*public UserDetails loadUserByUsername(String username);
	
	public List<GrantedAuthority> buildGrante(byte rol);
	
	public void crearUsuario(Usuarios usuario);*/
	
}
