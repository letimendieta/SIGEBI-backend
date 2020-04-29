package com.sigebi.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sigebi.model.Usuarios;
import com.sigebi.repo.IUsuariosRepo;

@SpringBootTest
class SigebiWebApplicationTests {

	@Autowired
	private IUsuariosRepo repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Test
	void crearUsuarioTest() {
		Usuarios us = new Usuarios();
		us.setUsuarioId(2);
		us.setCodigoUsuario("sigebi2");
		us.setPassword(encoder.encode("123"));
		Usuarios retorno = repo.save(us);
		
		assertTrue(retorno.getUsuarioId() == us.getUsuarioId());
	}

}
