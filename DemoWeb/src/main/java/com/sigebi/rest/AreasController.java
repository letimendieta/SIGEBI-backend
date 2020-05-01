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

import com.sigebi.model.Areas;
import com.sigebi.repo.IAreasRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/areas")
public class AreasController {

	@Autowired
	private IAreasRepo repo;

	@GetMapping
	public List<Areas> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Areas area) {
		repo.save(area);
	}

	@PutMapping
	public void modificar(@RequestBody Areas area) {
		repo.save(area);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
