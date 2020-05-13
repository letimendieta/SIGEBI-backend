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

import com.sigebi.dao.IHistorialClinicoDao;
import com.sigebi.entity.HistorialClinico;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/historial-Clinico")
public class HistorialClinicoController {

	@Autowired
	private IHistorialClinicoDao repo;

	@GetMapping
	public List<HistorialClinico> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody HistorialClinico historialClinico) {
		repo.save(historialClinico);
	}

	@PutMapping
	public void modificar(@RequestBody HistorialClinico historialClinico) {
		repo.save(historialClinico);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
