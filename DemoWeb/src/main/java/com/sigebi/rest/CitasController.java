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

import com.sigebi.model.Citas;
import com.sigebi.repo.ICitasRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/citas")
public class CitasController {

	@Autowired
	private ICitasRepo repo;

	@GetMapping
	public List<Citas> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Citas cita) {
		repo.save(cita);
	}

	@PutMapping
	public void modificar(@RequestBody Citas cita) {
		repo.save(cita);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
