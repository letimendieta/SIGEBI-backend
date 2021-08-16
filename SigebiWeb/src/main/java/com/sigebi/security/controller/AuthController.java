package com.sigebi.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigebi.entity.Personas;
import com.sigebi.entity.Usuarios;
import com.sigebi.security.dto.JwtDto;
import com.sigebi.security.dto.LoginUsuario;
import com.sigebi.security.dto.NuevoUsuario;
import com.sigebi.security.entity.Rol;
import com.sigebi.security.entity.Usuario;
import com.sigebi.security.enums.RolNombre;
import com.sigebi.security.jwt.JwtProvider;
import com.sigebi.security.service.RolService;
import com.sigebi.security.service.UsuarioService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.Mensaje;
import com.sigebi.util.exceptions.SigebiException;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;
    
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult result) throws SigebiException{
    	
    	Map<String, Object> response = new HashMap<>();	
    	
    	if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
    	
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getUsuario().getNombreUsuario()))
            return new ResponseEntity(new Mensaje("Nombre de usuario ya existe "), HttpStatus.BAD_REQUEST);
      
        usuarioService.save(nuevoUsuario);
        
        return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }
    
    @PostMapping("/actualizar")
    public ResponseEntity<?> actualizar(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult result) throws SigebiException{
    	Map<String, Object> response = new HashMap<>();	
    	
    	if( result.hasErrors() ) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
    	
        usuarioService.update(nuevoUsuario);
        
        return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) throws SigebiException{
        
    	JwtDto jwtDto = null;
    	    	
    	if( loginUsuario.getNombreUsuario().isEmpty() )
            return new ResponseEntity(new Mensaje("Ingrese un usuario"), HttpStatus.BAD_REQUEST);
    	
    	if( loginUsuario.getPassword().isEmpty() )
            return new ResponseEntity(new Mensaje("Ingrese una contraseña"), HttpStatus.BAD_REQUEST);
    	
    	if( loginUsuario.getNombreUsuario().isEmpty() || !usuarioService.existsByNombreUsuario(loginUsuario.getNombreUsuario()) )
            return new ResponseEntity(new Mensaje("Usuario incorrecto"), HttpStatus.UNAUTHORIZED);
    	
    	if( !usuarioService.esUsuarioActivo(loginUsuario.getNombreUsuario()) )
            return new ResponseEntity(new Mensaje("Usuario inactivo"), HttpStatus.UNAUTHORIZED);
    	
        try {
        	jwtDto = usuarioService.autenticar(loginUsuario);
		} catch (SigebiException e) {
			return new ResponseEntity(new Mensaje(e.getMessage()), HttpStatus.UNAUTHORIZED);
		}
    	
        
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }    
}
