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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IUsuariosDao;
import com.sigebi.entity.Usuarios;

@Service
public class UsuariosService implements UserDetailsService{

	@Autowired
	private IUsuariosDao repo;
	
	@Autowired
	private PasswordEncoder encoder;
		
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
	@Transactional
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
