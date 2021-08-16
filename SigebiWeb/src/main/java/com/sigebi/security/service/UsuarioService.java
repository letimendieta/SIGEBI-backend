package com.sigebi.security.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.clases.UsuarioRolId;
import com.sigebi.dao.IFuncionariosDao;
import com.sigebi.dao.IUsuarioDao;
import com.sigebi.dao.IUsuarioRolDao;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Usuarios;
import com.sigebi.security.dto.JwtDto;
import com.sigebi.security.dto.LoginUsuario;
import com.sigebi.security.dto.NuevoUsuario;
import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;
import com.sigebi.security.entity.UsuarioRol;
import com.sigebi.security.enums.RolNombre;
import com.sigebi.security.jwt.JwtProvider;
import com.sigebi.security.repository.UsuarioRepository;
import com.sigebi.service.FuncionariosService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;
import com.sigebi.util.exceptions.SigebiException.AuthenticationError;
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
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtProvider jwtProvider;

	@Autowired
	private IUsuarioDao usuarioDao;

	
    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }
    
    public boolean esUsuarioActivo(String nombreUsuario){
    	boolean estado = true;
    	
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(nombreUsuario);
        
        Usuario usuario = usuarioOpt.get();
        
        if( !Globales.Estados.ACTIVO.equals(usuario.getEstado())) {
        	estado = false;
        }
        
        return estado;
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
    	
    	String valido = validarPassword(nuevoUsuario.getUsuario().getPassword());
    	
    	if( valido != "" ) {
    		throw new SigebiException.BusinessException(valido);
    	}
    	
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
			
			String valido = validarPassword(nuevoUsuario.getUsuario().getPassword());
	    	
	    	if( valido != "" ) {
	    		throw new SigebiException.BusinessException(valido);
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
    
    public boolean validarContrasenha(LoginUsuario loginUsuario){
    	
    	Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(loginUsuario.getNombreUsuario());
    	
    	boolean valido = true;
    	Usuario usuario = usuarioOpt.get();
    	
    	String contraseña = passwordEncoder.encode(loginUsuario.getPassword());
    	
    	if( !usuario.getPassword().equals(contraseña) ) {
			valido = false;
		}
    	    		
    	return valido;
    }
    
    public JwtDto autenticar(LoginUsuario loginUsuario) throws SigebiException {
    	
    	Authentication authentication = null;
    	JwtDto jwtDto = null;
    	
    	try {
    		authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generateToken(authentication);
            UserDetails userDetails = (UserDetails)authentication.getPrincipal();
            jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
            
		} catch (AuthenticationException e) {
			throw new SigebiException.AuthenticationError("Contraseña incorrecta");
		}    	
    	    		
    	return jwtDto;
    }
    
    public String validarPassword(String pass) {
    	String valido = "";
        char clave;
        byte  contNumero = 0, contLetraMay = 0, contLetraMin=0;
        for (byte i = 0; i < pass.length(); i++) {
                 clave = pass.charAt(i);
                String passValue = String.valueOf(clave);
                 /*if (passValue.matches("[A-Z]")) {
                     contLetraMay++;
                 }*/
                 if (passValue.matches("[a-z]")) {
                     contLetraMin++;
                 } else if (passValue.matches("[0-9]")) {
                     contNumero++;
                 }
         }
         int passLength = pass.length();
         //System.out.println("Cantidad de letras mayusculas:"+contLetraMay);
         System.out.println("Cantidad de letras minusculas:"+contLetraMin);
         System.out.println("Cantidad de numeros:"+contNumero);
         
         if( contLetraMin == 0 ) {
        	 valido = valido + " La contraseña debe contener letras";
         }
         if( contNumero == 0) {
        	 valido = valido + " La contraseña debe contener números";
         }
         if( passLength < 8) {
        	 valido = valido + " La contraseña debe contener como mínimo 8 caracteres";
         }
         
         return valido;
    }
		
}
