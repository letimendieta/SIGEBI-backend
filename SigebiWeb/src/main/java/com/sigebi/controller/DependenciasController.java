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

import com.sigebi.dao.IDependenciasDao;
import com.sigebi.entity.Dependencias;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dependencias")
public class DependenciasController {

	@Autowired
	private IDependenciasDao repo;

	@GetMapping
	public List<Dependencias> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Dependencias dependencia) {
		repo.save(dependencia);
	}

	@PutMapping
	public void modificar(@RequestBody Dependencias dependencia) {
		repo.save(dependencia);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
