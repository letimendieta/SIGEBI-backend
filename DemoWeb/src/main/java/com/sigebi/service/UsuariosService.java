package com.sigebi.service;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sigebi.model.Usuarios;
import com.sigebi.repo.IUsuariosRepo;

@Service
public class UsuariosService implements UserDetailsService{

	@Autowired
	private IUsuariosRepo repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
		
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuarios us = repo.findByCodigoUsuario(username);
		
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		
	    UserDetails userDet = new User(us.getCodigoUsuario(), us.getPassword(), roles);
	    
	    return userDet;
	}

	public List<GrantedAuthority> buildGrante(byte rol){
		return null;
	}
	
	public void crearUsuario(Usuarios usuario){
		try {
			String encodePass = encoder.encode(usuario.getPassword());	
			usuario.setPassword(encodePass);
			repo.save(usuario);
		} catch (Exception e) {
			throw new AbortException("Ocurrio un error al guardar el usuario: " + e.getMessage());
		}
		
	}
}
