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

import com.sigebi.model.HorariosDisponibles;
import com.sigebi.repo.IHorariosDisponiblesRepo;

@RestController
@RequestMapping("/horarios-Disponibles")
public class HorariosDisponiblesController {

	@Autowired
	private IHorariosDisponiblesRepo repo;

	@GetMapping
	public List<HorariosDisponibles> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody HorariosDisponibles horarioDisponible) {
		repo.save(horarioDisponible);
	}

	@PutMapping
	public void modificar(@RequestBody HorariosDisponibles horarioDisponible) {
		repo.save(horarioDisponible);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
