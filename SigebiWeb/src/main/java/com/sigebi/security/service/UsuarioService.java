package com.sigebi.security.service;

import com.sigebi.dao.IUsuarioDao;
import com.sigebi.dao.IUsuariosDao;
import com.sigebi.entity.Usuarios;
import com.sigebi.security.entity.Usuario;
import com.sigebi.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

	@Autowired
	private IUsuarioDao usuarioDao;

    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }


    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }
    
		
}
