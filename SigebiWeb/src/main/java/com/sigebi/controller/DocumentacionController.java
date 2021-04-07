package com.sigebi.controller;

import java.net.URISyntaxException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/swagger")
public class DocumentacionController {
	
	@ApiOperation(value = "Redirecciona a la documentación de los apis de sigebi.")
	@RequestMapping (value = "", method = RequestMethod.GET)
	public String redirectToExternalUrl() throws URISyntaxException {
		return "redirect:/swagger-ui.html";
	}
}
