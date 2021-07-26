package com.sigebi.security.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigebi.security.entity.Rol;
import com.sigebi.security.service.RolService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@RequestMapping("/auth/rol")
@CrossOrigin
public class RolController {
   
    @Autowired
    RolService rolService;

    
    @GetMapping
	public ResponseEntity<List<Rol>> listar() throws SigebiException {
		List<Rol> rolList = null;
		
		rolList = rolService.listar();
		
		return new ResponseEntity<List<Rol>>(rolList, HttpStatus.OK);
	}
    
    @GetMapping(value = "/{id}")
	public ResponseEntity<List<Rol>> obtenerRolesUsuario(@PathVariable("id") Integer id) throws SigebiException {
		List<Rol> rolList = null;
		
		rolList = rolService.listar();
		
		return new ResponseEntity<List<Rol>>(rolList, HttpStatus.OK);
	}
    
}
