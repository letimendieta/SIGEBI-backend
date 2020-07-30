package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sigebi.entity.Usuarios;


public interface UsuariosService {

	public List<Usuarios> findAll();
	
	public Usuarios findById(int id);	
	
	public Usuarios guardar(Usuarios usuario) throws Exception;
	
	public Usuarios actualizar(Usuarios usuario) throws Exception;
	
	public void delete(int id);
	
	public List<Usuarios> buscar(Date fromDate, Date toDate, Usuarios usuario, List<Integer> personasId, Pageable pageable);
	
	public UserDetails loadUserByUsername(String username);
	
	public List<GrantedAuthority> buildGrante(byte rol);
	
	public void crearUsuario(Usuarios usuario);
	
}
