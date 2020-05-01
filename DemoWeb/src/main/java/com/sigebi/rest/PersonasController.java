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

import com.sigebi.model.Personas;
import com.sigebi.repo.IPersonasRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/personas")
public class PersonasController {

	@Autowired
	private IPersonasRepo repo;

	@GetMapping
	public List<Personas> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Personas persona) {
		repo.save(persona);
	}

	@PutMapping
	public void modificar(@RequestBody Personas persona) {
		repo.save(persona);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
