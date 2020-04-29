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

import com.sigebi.model.DatosConsultas;
import com.sigebi.repo.IDatosConsultasRepo;

@RestController
@RequestMapping("/datos-consultas")
public class DatosConsultasController {

	@Autowired
	private IDatosConsultasRepo repo;

	@GetMapping
	public List<DatosConsultas> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody DatosConsultas datoConsulta) {
		repo.save(datoConsulta);
	}

	@PutMapping
	public void modificar(@RequestBody DatosConsultas datoConsulta) {
		repo.save(datoConsulta);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
