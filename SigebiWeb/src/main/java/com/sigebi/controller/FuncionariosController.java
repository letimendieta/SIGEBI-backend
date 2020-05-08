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

import com.sigebi.model.Funcionarios;
import com.sigebi.service.IFuncionariosRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/funcionarios")
public class FuncionariosController {

	@Autowired
	private IFuncionariosRepo repo;

	@GetMapping
	public List<Funcionarios> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Funcionarios funcionario) {
		repo.save(funcionario);
	}

	@PutMapping
	public void modificar(@RequestBody Funcionarios funcionario) {
		repo.save(funcionario);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
