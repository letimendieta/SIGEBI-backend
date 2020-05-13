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

import com.sigebi.dao.IProcedimientosDao;
import com.sigebi.entity.Procedimientos;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/procedimientos")
public class ProcedimientosController {

	@Autowired
	private IProcedimientosDao repo;

	@GetMapping
	public List<Procedimientos> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Procedimientos procedimiento) {
		repo.save(procedimiento);
	}

	@PutMapping
	public void modificar(@RequestBody Procedimientos procedimiento) {
		repo.save(procedimiento);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
