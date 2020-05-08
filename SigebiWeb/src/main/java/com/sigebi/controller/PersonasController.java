package com.sigebi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sigebi.model.Personas;
import com.sigebi.service.IPersonasRepo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/personas")
public class PersonasController {

	@Autowired
	private IPersonasRepo repo;

	@GetMapping
	public ResponseEntity<List<Personas>> listar() {
		List<Personas> lista = repo.findAll();
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Optional<Personas>> listar(@PathVariable("id") Integer id){
		Optional<Personas> lista = repo.findById(id);
		return ResponseEntity.ok(lista);
	}

	@PostMapping
	public ResponseEntity<String> insertar(@RequestBody Personas persona) {
		repo.save(persona);
		return ResponseEntity.ok("OK");
	}

	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Personas persona) {
		repo.save(persona);
		return ResponseEntity.ok("OK");
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
		return ResponseEntity.ok("OK");
	}
}
