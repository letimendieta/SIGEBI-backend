package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.aspectj.bridge.AbortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IUsuarioDao;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.entity.Usuarios;
import com.sigebi.security.entity.Usuario;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UsuariosService;

@Service
public class UsuariosServiceImpl implements UsuariosService, UserDetailsService{
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	//@Autowired
	//private BCryptPasswordEncoder encoder;
	
	//@Autowired
	//private PersonasService personasService;
	
	public UsuariosServiceImpl(IUsuarioDao usuariosDao) {
        this.usuarioDao = usuariosDao;
    }
	
	/*@Override
	@Transactional(readOnly = true)
	public List<Usuarios> findAll() {
		return (List<Usuarios>) usuariosDao.findAll();
	}*/
	
	@Override
	@Transactional(readOnly = true)
	public Usuario findById(int id) {
		return usuarioDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Usuario findByFuncionario(Funcionarios funcionario) {
		return usuarioDao.findByFuncionarios(funcionario);
	}
	
	/*@Transactional
	public Usuarios guardar(Usuarios usuario) throws Exception {
		
		//Buscar si la persona ya es usuario
		List<Usuarios> usuarioDb = usuariosDao.findByPersonas(usuario.getPersonas());
		
		if( !usuarioDb.isEmpty() ) {
			throw new Exception("La persona ya existe como usuario");
		}
		
		if( usuario.getPersonas().getPersonaId() != null ) {
			//Busca a la persona y si existe actualizar sus datos
			Personas persona = personasService.obtener(usuario.getPersonas().getPersonaId());
			
			if(persona == null) {
				throw new Exception("No se encontro persona con id: " + usuario.getPersonas().getPersonaId());
			}
		}else {
			throw new Exception("Id de la persona es requerido ");
		}		
		//codificar la contrasenha
		//String encodePass = encoder.encode(usuario.getPassword());	
		//usuario.setPassword(encodePass);
				
		return usuariosDao.save(usuario);
	}
	
	@Transactional
	public Usuarios actualizar(Usuarios usuario) throws Exception {						
		
		if(usuario.getFuncionarios() != null && usuario.getFuncionarios().getFuncionarioId() == null) {
			usuario.setFuncionarios(null);
		}		
		//codificar la contrasenha
		//String encodePass = encoder.encode(usuario.getPassword());	
		//usuario.setPassword(encodePass);
		
		return usuariosDao.save(usuario);
	}
	
	@Override
	@Transactional
	public void delete(int id) {
		usuariosDao.deleteById(id);
	}*/
	
	/*@Override
	@Transactional(readOnly = true)
	public List<Usuarios> buscar(Date fromDate, Date toDate, Usuarios usuario, List<Integer> personasId, Pageable pageable) {
		List<Usuarios> UsuariosList = usuariosDao.findAll((Specification<Usuarios>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( usuario.getUsuarioId() != null ) {
                p = cb.and(p, cb.equal(root.get("usuarioId"), usuario.getUsuarioId()) );
            }
            if ( usuario.getCodigoUsuario() != null ) {
                p = cb.and(p, cb.equal(root.get("codigoUsuario"), usuario.getCodigoUsuario()) );
            }
            if ( usuario.getEstado()!= null ) {
                p = cb.and(p, cb.equal(root.get("estado"), usuario.getEstado()) );
            }
            cq.orderBy(cb.desc(root.get("usuarioId")));
            return p;
        }, pageable).getContent();
        return UsuariosList;
    }*/
	
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> buscar(Date fromDate, Date toDate, Usuario usuario, List<Integer> personasId, Pageable pageable) {
		List<Usuario> UsuariosList = usuarioDao.findAll((Specification<Usuario>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( usuario.getId() != 0 ) {
                p = cb.and(p, cb.equal(root.get("id"), usuario.getId()) );
            }
            if ( usuario.getNombreUsuario() != null ) {
                p = cb.and(p, cb.equal(root.get("nombreUsuario"), usuario.getNombreUsuario()) );
            }
            if ( usuario.getEstado()!= null ) {
                p = cb.and(p, cb.equal(root.get("estado"), usuario.getEstado()) );
            }
            cq.orderBy(cb.desc(root.get("id")));
            return p;
        }, pageable).getContent();
		
		for( Usuario usu : UsuariosList ){
			usu.setPassword(null);
		}
		
        return UsuariosList;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
		
	/*@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuarios us = usuariosDao.findByCodigoUsuario(username);
		
		List<GrantedAuthority> roles = new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		
	    UserDetails userDet = new User(us.getCodigoUsuario(), us.getPassword(), roles);
	    
	    return userDet;
	}
	
	@Override
	public List<GrantedAuthority> buildGrante(byte rol){
		return null;
	}
	
	@Override
	public void crearUsuario(Usuarios usuario){
		try {
			//String encodePass = encoder.encode(usuario.getPassword());	
			//usuario.setPassword(encodePass);
			usuariosDao.save(usuario);
		} catch (Exception e) {
			throw new AbortException("Ocurrio un error al guardar el usuario: " + e.getMessage());
		}		
	}*/
}
