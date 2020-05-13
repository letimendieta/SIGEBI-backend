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

import com.sigebi.dao.IPacientesDao;
import com.sigebi.entity.Pacientes;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pacientes")
public class PacientesController {

	@Autowired
	private IPacientesDao repo;

	@GetMapping
	public List<Pacientes> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Pacientes paciente) {
		repo.save(paciente);
	}

	@PutMapping
	public void modificar(@RequestBody Pacientes paciente) {
		repo.save(paciente);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
	
	/*@GetMapping("/obtener")
	public List<Pacientes> obtener(@RequestBody Pacientes paciente) {
		return repo.obtenerPaciente();
	}*/
}
