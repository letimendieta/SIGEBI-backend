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

import com.sigebi.model.Departamentos;
import com.sigebi.repo.IDepartamentosRepo;

@RestController
@RequestMapping("/departamentos")
public class DepartamentosController {

	@Autowired
	private IDepartamentosRepo repo;

	@GetMapping
	public List<Departamentos> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Departamentos departamento) {
		repo.save(departamento);
	}

	@PutMapping
	public void modificar(@RequestBody Departamentos departamento) {
		repo.save(departamento);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
