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

import com.sigebi.model.Carreras;
import com.sigebi.repo.ICarrerasRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/carreras")
public class CarrerasController {

	@Autowired
	private ICarrerasRepo repo;

	@GetMapping
	public List<Carreras> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Carreras carrera) {
		repo.save(carrera);
	}

	@PutMapping
	public void modificar(@RequestBody Carreras carrera) {
		repo.save(carrera);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
