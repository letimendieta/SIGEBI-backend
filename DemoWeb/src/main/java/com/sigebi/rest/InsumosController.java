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

import com.sigebi.model.Insumos;
import com.sigebi.repo.IInsumosRepo;

@RestController
@RequestMapping("/Insumos")
public class InsumosController {

	@Autowired
	private IInsumosRepo repo;

	@GetMapping
	public List<Insumos> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Insumos insumo) {
		repo.save(insumo);
	}

	@PutMapping
	public void modificar(@RequestBody Insumos insumo) {
		repo.save(insumo);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
