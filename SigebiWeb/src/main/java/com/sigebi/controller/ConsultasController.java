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

import com.sigebi.dao.IConsultasDao;
import com.sigebi.entity.Consultas;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/consultas")
public class ConsultasController {

	@Autowired
	private IConsultasDao repo;

	@GetMapping
	public List<Consultas> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Consultas datoConsulta) {
		repo.save(datoConsulta);
	}

	@PutMapping
	public void modificar(@RequestBody Consultas datoConsulta) {
		repo.save(datoConsulta);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
