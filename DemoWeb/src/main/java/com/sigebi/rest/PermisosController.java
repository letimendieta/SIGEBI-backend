package com.sigebi.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigebi.model.Permisos;
import com.sigebi.repo.IPermisosRepo;

@RestController
@RequestMapping("/permisos")
public class PermisosController {

	@Autowired
	private IPermisosRepo repo;

	@GetMapping
	public List<Permisos> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Permisos permiso) {
		repo.save(permiso);
	}

	@PutMapping
	public void modificar(@RequestBody Permisos permiso) {
		repo.save(permiso);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
