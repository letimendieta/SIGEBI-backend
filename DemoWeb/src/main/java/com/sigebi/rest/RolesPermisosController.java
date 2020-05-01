package com.sigebi.rest;

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

import com.sigebi.model.RolesPermisos;
import com.sigebi.repo.IRolesPermisosRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/roles-Permisos")
public class RolesPermisosController {

	@Autowired
	private IRolesPermisosRepo repo;

	@GetMapping
	public List<RolesPermisos> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody RolesPermisos rolPermiso) {
		repo.save(rolPermiso);
	}

	@PutMapping
	public void modificar(@RequestBody RolesPermisos rolPermiso) {
		repo.save(rolPermiso);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
