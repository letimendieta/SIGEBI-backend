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

import com.sigebi.dao.IStockDao;
import com.sigebi.entity.Stock;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/stock")
public class StockController {

	@Autowired
	private IStockDao repo;

	@GetMapping
	public List<Stock> listar() {
		return repo.findAll();
	}

	@PostMapping
	public void insertar(@RequestBody Stock stock) {
		repo.save(stock);
	}

	@PutMapping
	public void modificar(@RequestBody Stock stock) {
		repo.save(stock);
	}

	@DeleteMapping(value = "/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		repo.deleteById(id);
	}
}
