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

import com.sigebi.model.Usuarios;
import com.sigebi.service.IUsuariosRepo;
import com.sigebi.serviceImplement.UsuariosService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	private IUsuariosRepo repo;
	
	@Autowired
	private UsuariosService usuarioService;

	@GetMapping
	public List<Usuarios> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Usuarios usuario) {
		usuarioService.crearUsuario(usuario);
	}

	@PutMapping
	public void modificar(@RequestBody Usuarios usuario) {
		repo.save(usuario);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
