package com.sigebi.security.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.clases.UsuarioRolId;
import com.sigebi.dao.IFuncionariosDao;
import com.sigebi.dao.IUsuarioDao;
import com.sigebi.dao.IUsuarioRolDao;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Usuarios;
import com.sigebi.security.dto.NuevoUsuario;
import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;
import com.sigebi.security.entity.UsuarioRol;
import com.sigebi.security.enums.RolNombre;
import com.sigebi.security.repository.UsuarioRepository;
import com.sigebi.service.FuncionariosService;
import com.sigebi.util.exceptions.SigebiException;
import com.sigebi.util.exceptions.SigebiException.BusinessException;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RolService rolService;
    
    @Autowired
	private FuncionariosService funcionariosService;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private IUsuarioRolDao usuarioRolDao;
	
    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }


    public void save(NuevoUsuario nuevoUsuario) throws SigebiException{
    	
    	if(  nuevoUsuario.getUsuario().getFuncionarios() == null 
    			|| nuevoUsuario.getUsuario().getFuncionarios().getFuncionarioId() == null ){
    		throw new SigebiException.BusinessException("Datos del funcionario es requerido ");
    	}
    	Funcionarios funcionario = new Funcionarios();
    	funcionario.setFuncionarioId(nuevoUsuario.getUsuario().getFuncionarios().getFuncionarioId());
    	  
    	Usuario usuarioDb = usuarioDao.findByFuncionarios(funcionario);
    	
    	if( usuarioDb != null ) {
    		throw new SigebiException.BusinessException("El funcionario ya cuenta con un usuario ");
    	}
    	
    	Usuario usuario = nuevoUsuario.getUsuario();
    	
    	usuario.setPassword(passwordEncoder.encode(nuevoUsuario.getUsuario().getPassword()));
        Set<Rol> roles = new HashSet<>();
        
        for(Rol rol : nuevoUsuario.getRolesList()) {
        	roles.add(rol);
        }
        usuario.setRoles(roles);
    	
        usuarioRepository.save(usuario);
    }
    
    public void update(NuevoUsuario nuevoUsuario) throws SigebiException{
    	
    	try {
			if( nuevoUsuario.getUsuario().getFuncionarios() == null 
					|| nuevoUsuario.getUsuario().getFuncionarios().getFuncionarioId() == null ){
				throw new SigebiException.BusinessException("Datos del funcionario es requerido ");
			}
			Funcionarios funcionario = new Funcionarios();
			funcionario.setFuncionarioId(nuevoUsuario.getUsuario().getFuncionarios().getFuncionarioId());
			  
			Usuario usuarioDb = usuarioDao.findByFuncionarios(funcionario);
			
			if( usuarioDb == null ) {
				throw new SigebiException.BusinessException("No se encontró usuario ");
			}
			    	
			if( nuevoUsuario.isEsCambioContrasenha() ) {
				usuarioDb.setPassword(passwordEncoder.encode(nuevoUsuario.getUsuario().getPassword()));
			}
						
			Set<Rol> roles = new HashSet<>();
      
			for(Rol rol : nuevoUsuario.getRolesList()) {
				roles.add(rol);
			}
			usuarioDb.setRoles(roles);
			
			usuarioRepository.save(usuarioDb);
			
		} catch (Exception e) {
			throw new SigebiException.InternalServerError("Error al actualizar los datos del usuario: " + e.getMessage());
		}
    }
		
}
