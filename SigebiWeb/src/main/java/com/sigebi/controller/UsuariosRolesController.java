package com.sigebi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigebi.dao.IUsuarioRolDao;
import com.sigebi.entity.UsuarioRol;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/UsuariosRoles")
public class UsuariosRolesController {

	@Autowired
	private IUsuarioRolDao repo;

	/*@GetMapping
	public List<UsuarioRol> listar() {
		return repo.findAll();
	}*/

	@PostMapping
	public void insertar(@RequestBody UsuarioRol usuarioRol) {
		repo.save(usuarioRol);
	}

	@PutMapping
	public void modificar(@RequestBody UsuarioRol usuarioRol) {
		repo.save(usuarioRol);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
