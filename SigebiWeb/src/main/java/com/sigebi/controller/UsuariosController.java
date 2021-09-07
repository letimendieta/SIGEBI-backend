package com.sigebi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.security.entity.Usuario;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UsuariosService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/usuarios")
public class UsuariosController {
	@Autowired
	private UsuariosService usuariosService;
	@Autowired
	private PersonasService personasService;
	@Autowired
	private FuncionariosService funcionariosService;
	@Autowired
	private UtilesService utiles;
	
	private static final String DATE_PATTERN = "yyyy/MM/dd";

	public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }
	
	
	@GetMapping(value = "/generar/{personaId}")
	public ResponseEntity generarNombreUsuario(@PathVariable("personaId") Integer personaId) throws SigebiException {

		String nombreUsuario = usuariosService.generarNombreUsuario(personaId);
		
		Usuario usu = new Usuario();
		usu.setNombreUsuario(nombreUsuario);
		
		return new ResponseEntity(usu, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> obtener(@PathVariable("id") Integer id){
		Map<String, Object> response = new HashMap<>();
		Usuario usuario = null;

		usuario = usuariosService.findById(id);
		
		if( usuario == null ) {
			response.put("mensaje", "El usuario con ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	@GetMapping("/buscar")
    public ResponseEntity<?> buscarUsuarios(
    		@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) Date toDate,
            @RequestParam(required = false) String filtros,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDir,
            Pageable pageable) throws JsonMappingException, JsonProcessingException{
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Usuario usuario = null;
		if(!utiles.isNullOrBlank(filtros)) {
			usuario = objectMapper.readValue(filtros, Usuario.class);
		}				
		
		List<Usuario> usuariosList = new ArrayList<Usuario>();
		
		if ( usuario == null ) {
			usuario = new Usuario();
		}
		List<Personas> personasList = new ArrayList<Personas>();
		List<Funcionarios> funcionariosList = new ArrayList<Funcionarios>();
		List<Integer> personasIds = new ArrayList<Integer>();
		List<Integer> funcionariosId = new ArrayList<Integer>();
		if( usuario.getFuncionarios() != null && usuario.getFuncionarios().getPersonas() != null) {
			personasList = personasService.buscarNoPaginable(fromDate, toDate, usuario.getFuncionarios().getPersonas());

			if(personasList != null && personasList.size() > 0) {
				for( Personas persona : personasList ){
					personasIds.add(persona.getPersonaId());
				}
				funcionariosList = funcionariosService.buscarNoPaginable(null, null, null, personasIds);
				
				if( personasList.isEmpty()) {
					return new ResponseEntity<List<Usuario>>(usuariosList, HttpStatus.OK);
				}
				for( Funcionarios funcionario : funcionariosList ){
					funcionariosId.add(funcionario.getFuncionarioId());
				}
			}else {
				return new ResponseEntity<List<Usuario>>(usuariosList, HttpStatus.OK);
			}			
		}
		
		if ("-1".equals(size)) {
			int total = usuariosService.count();
			int pagina = page != null ? Integer.parseInt(page) : 0;
			pageable = PageRequest.of(pagina, total);
		}
		
		usuariosList = usuariosService.buscar(fromDate, toDate, usuario, funcionariosId, orderBy, orderDir, pageable);
						
        return new ResponseEntity<List<Usuario>>(usuariosList, HttpStatus.OK);
    }

}
