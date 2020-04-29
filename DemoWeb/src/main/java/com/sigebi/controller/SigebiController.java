package com.sigebi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sigebi.model.Personas;
import com.sigebi.repo.IPersonasRepo;

@Controller
public class SigebiController {
	
	@Autowired
	private IPersonasRepo repo;
	
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		
		Personas p = new Personas();
		p.setCedula("4695968");
		p.setNombres("Cristin");
		p.setApellidos("Jara");
		repo.save(p);
		
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@GetMapping("/listar")
	public String greeting(Model model){		
		
		model.addAttribute("personas", repo.findAll());
		return "greeting";
	}
}
