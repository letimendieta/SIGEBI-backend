package com.sigebi.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.UsuarioPrincipal;
import com.sigebi.security.enums.RolNombre;
import com.sigebi.security.repository.RolRepository;

@Service
@Transactional
public class RolService {

    @Autowired
    RolRepository rolRepository;

    public Optional<Rol> getByRolNombre(RolNombre rolNombre){
        return rolRepository.findByRolNombre(rolNombre);
    }
    
    public List<Rol> listar(){    	
        return rolRepository.findByEstado("A");
    }

    public void save(Rol rol){
        rolRepository.save(rol);
    }
    
    public boolean verificarRol(String rol) {
    	
    	boolean autorized = false;
    	
    	Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
		
		UsuarioPrincipal userDetails = (UsuarioPrincipal) auth.getPrincipal();
					
		for( int i= 0; i < userDetails.getAuthorities().size() ; i++){
			if( userDetails.getAuthorities().toArray()[i].toString().equals(rol) ) {
				autorized = true;
				break;
			}
		}
		
		return autorized;
    }
}
