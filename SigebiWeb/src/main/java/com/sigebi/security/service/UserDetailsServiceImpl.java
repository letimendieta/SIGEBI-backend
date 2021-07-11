package com.sigebi.security.service;

import com.sigebi.security.entity.Usuario;
import com.sigebi.security.entity.UsuarioPrincipal;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
    	Usuario usuario = null;
    	Optional<Usuario> usr = usuarioService.getByNombreUsuario(nombreUsuario);
    	if( usr.isPresent() ){
    		usuario = usr.get();
    	}    	
        return UsuarioPrincipal.build(usuario);
    }
}
