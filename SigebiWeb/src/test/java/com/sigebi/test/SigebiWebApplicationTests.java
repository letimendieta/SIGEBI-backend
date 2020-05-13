package com.sigebi.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sigebi.dao.IAreasDao;
import com.sigebi.dao.IUsuariosDao;
import com.sigebi.entity.Areas;

@SpringBootTest
class SigebiWebApplicationTests {

	@Autowired
	private IUsuariosDao repo;
	
	@Autowired
	private IAreasDao areasRepo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Test
	void crearUsuarioTest() {
		/*Usuarios us = new Usuarios();
		us.setUsuarioId(2);
		us.setCodigoUsuario("sigebi2");
		us.setPassword(encoder.encode("123"));
		Usuarios retorno = repo.save(us);*/
		
		Areas ar = new Areas();
		ar.setCodigo("MED");;
		ar.setDescripcion("Clinica");
		ar.setEstado("A");
		Areas retorno = areasRepo.save(ar);
		
		
		assertTrue(retorno.getCodigo() == ar.getCodigo());
	}

}
