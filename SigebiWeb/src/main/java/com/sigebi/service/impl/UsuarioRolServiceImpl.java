package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IUsuarioRolDao;
import com.sigebi.entity.UsuarioRol;
import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;
import com.sigebi.service.UsuarioRolService;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService{
	
	@Autowired
	private IUsuarioRolDao usuarioRolDao;
	
	//@Autowired
	//private BCryptPasswordEncoder encoder;
	
	//@Autowired
	//private PersonasService personasService;
	
	public UsuarioRolServiceImpl(IUsuarioRolDao usuarioRolDao) {
        this.usuarioRolDao = usuarioRolDao;
    }
	
	/*@Override
	@Transactional(readOnly = true)
	public List<Usuarios> findAll() {
		return (List<Usuarios>) usuariosDao.findAll();
	}*/
	
	@Override
	@Transactional(readOnly = true)
	public List<UsuarioRol> findByUsuario(Usuario usuario) {
		return usuarioRolDao.findByUsuario(usuario);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> listarRolesUsuario(Usuario usuario) {
		List<UsuarioRol> usuariosRoles = usuarioRolDao.findByUsuario(usuario);
		List<String> listaRoles = new ArrayList<String>();
		
		for(UsuarioRol usuRol : usuariosRoles  ) {
			listaRoles.add(usuRol.getRol().getRolNombre().toString());
		}
		return listaRoles;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UsuarioRol> findByRol(Rol rol) {
		return usuarioRolDao.findByRol(rol);
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
	
	/*@Override
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
