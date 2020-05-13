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

import com.sigebi.dao.IEstamentosDao;
import com.sigebi.entity.Estamentos;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/Estamentos")
public class EstamentosController {

	@Autowired
	private IEstamentosDao repo;

	@GetMapping
	public List<Estamentos> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Estamentos estamento) {
		repo.save(estamento);
	}

	@PutMapping
	public void modificar(@RequestBody Estamentos estamento) {
		repo.save(estamento);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
