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

import com.sigebi.model.Pacientes;
import com.sigebi.repo.IPacientesRepo;

@RestController
@RequestMapping("/pacientes")
public class PacientesController {

	@Autowired
	private IPacientesRepo repo;

	@GetMapping
	public List<Pacientes> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Pacientes per) {
		repo.save(per);
	}

	@PutMapping
	public void modificar(@RequestBody Pacientes per) {
		repo.save(per);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
