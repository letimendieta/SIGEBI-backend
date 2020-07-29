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
	/*@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuarios us = repo.findByCodigoUsuario(username);
		
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		
	    UserDetails userDet = new User(us.getCodigoUsuario(), us.getPassword(), roles);
	    
	    return userDet;
	}

	/*public List<GrantedAuthority> buildGrante(byte rol){
		return null;
	}
	/*@Transactional
	public void crearUsuario(Usuarios usuario){
		try {
			String encodePass = encoder.encode(usuario.getPassword());	
			usuario.setPassword(encodePass);
			repo.save(usuario);
		} catch (Exception e) {
			throw new AbortException("Ocurrio un error al guardar el usuario: " + e.getMessage());
		}
		
	}*/
}
