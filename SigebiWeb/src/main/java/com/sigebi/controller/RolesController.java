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

import com.sigebi.model.Roles;
import com.sigebi.service.IRolesRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/roles")
public class RolesController {

	@Autowired
	private IRolesRepo repo;

	@GetMapping
	public List<Roles> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Roles rol) {
		repo.save(rol);
	}

	@PutMapping
	public void modificar(@RequestBody Roles rol) {
		repo.save(rol);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
