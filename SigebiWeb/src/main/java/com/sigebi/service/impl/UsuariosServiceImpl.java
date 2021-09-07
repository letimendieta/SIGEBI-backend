package com.sigebi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.dao.IUsuarioDao;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.security.entity.Usuario;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UsuariosService;
import com.sigebi.util.exceptions.SigebiException;

@Service
public class UsuariosServiceImpl implements UsuariosService, UserDetailsService{
	
	@Autowired
	private IUsuarioDao usuarioDao;
		
	@Autowired
	private PersonasService personasService;
	
	public UsuariosServiceImpl(IUsuarioDao usuariosDao) {
        this.usuarioDao = usuariosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) usuarioDao.count();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Usuario findById(int id) {
		Usuario usuario = usuarioDao.findById(id).orElse(null);	
		usuario.setPassword(null);
		return usuario;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Usuario findByFuncionario(Funcionarios funcionario) {
		return usuarioDao.findByFuncionarios(funcionario);
	}
	
	public String generarNombreUsuario(Integer personaId) throws SigebiException{
		
		if( personaId == null ) {
			throw new SigebiException.BusinessException("Persona id es requerido");
		}
		
		Personas persona = personasService.obtener(personaId);
		
		if( persona == null ) {
			throw new SigebiException.BusinessException("No se encontro datos de la persona ");
		}
		
		if( persona.getNombres() == null || persona.getNombres().isEmpty() ) {
			throw new SigebiException.BusinessException("Se necesita nombre de la persona ");
		}
		if( persona.getApellidos() == null || persona.getApellidos().isEmpty() ) {
			throw new SigebiException.BusinessException("Se necesita apellido de la persona");
		}
					    
	    String[] nombres = persona.getNombres().split(" ");
	    String[] apellidos = persona.getApellidos().split(" ");
	 
	    String nombreUsuario = ( apellidos[0] + nombres[0].charAt(0) ).toLowerCase();
		
		return nombreUsuario;
		
	}
		
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> buscar(Date fromDate, Date toDate, Usuario usuario, List<Integer> funcionariosId, String orderBy, String orderDir, Pageable pageable){
		List<Usuario> usuariosList;
		
		Specification<Usuario> usuarioSpec = (Specification<Usuario>) (root, cq, cb) -> {
		            
			Predicate p = cb.conjunction();
            if( funcionariosId != null && !funcionariosId.isEmpty() ){
            	p = cb.and(root.get("funcionarios").in(funcionariosId));
            }          
            if ( usuario.getFuncionarios() != null && usuario.getFuncionarios().getFuncionarioId() != null) {
                p = cb.and(p, cb.equal(root.get("funcionarios"), usuario.getFuncionarios().getFuncionarioId()) );
            }
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( usuario.getId() != 0 ) {
                p = cb.and(p, cb.equal(root.get("id"), usuario.getId()) );
            }
            if ( !StringUtils.isEmpty(usuario.getNombreUsuario())) {
                p = cb.and(p, cb.like(cb.lower(root.get("nombreUsuario")), "%" + usuario.getNombreUsuario().toLowerCase() + "%"));
            }
            if ( !StringUtils.isEmpty(usuario.getEstado()) ) {
                p = cb.and(p, cb.equal(root.get("estado"), usuario.getEstado()) );
            }
            String orden = "id";
            if (!StringUtils.isEmpty(orderBy)) {
            	orden = orderBy;
            }
            if("asc".equalsIgnoreCase(orderDir)){
            	cq.orderBy(cb.asc(root.get(orden)));
            }else {
            	cq.orderBy(cb.desc(root.get(orden)));
            }
            return p;
        };
        
        if(pageable != null) {
        	usuariosList = usuarioDao.findAll(usuarioSpec, pageable).getContent();			
		}else {
			usuariosList = usuarioDao.findAll(usuarioSpec);
		}
		
		for( Usuario usu : usuariosList ){
			usu.setPassword(null);
		}
		
        return usuariosList;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
