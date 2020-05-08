package com.sigebi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigebi.model.UsuariosRoles;
import com.sigebi.service.IUsuariosRolesRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/UsuariosRoles")
public class UsuariosRolesController {

	@Autowired
	private IUsuariosRolesRepo repo;

	@GetMapping
	public List<UsuariosRoles> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody UsuariosRoles usuarioRol) {
		repo.save(usuarioRol);
	}

	@PutMapping
	public void modificar(@RequestBody UsuariosRoles usuarioRol) {
		repo.save(usuarioRol);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
