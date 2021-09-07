package com.sigebi.security.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigebi.security.dto.JwtDto;
import com.sigebi.security.dto.LoginUsuario;
import com.sigebi.security.dto.NuevoUsuario;
import com.sigebi.security.jwt.JwtProvider;
import com.sigebi.security.service.RolService;
import com.sigebi.security.service.UsuarioService;
import com.sigebi.util.Mensaje;
import com.sigebi.util.exceptions.SigebiException;

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
        
        return new ResponseEntity(new Mensaje("Usuario guardado"), HttpStatus.CREATED);
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
        
        return new ResponseEntity(new Mensaje("Usuario actualizado"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) throws Exception{
        
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
