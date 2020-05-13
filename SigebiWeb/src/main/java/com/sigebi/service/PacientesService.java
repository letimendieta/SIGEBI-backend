package com.sigebi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sigebi.dao.IPacientesDao;
import com.sigebi.entity.Pacientes;

@Service
public class PacientesService{

	@Autowired
	private IPacientesDao repo;
	
		
	public List<Pacientes> obtenerPaciente(Pacientes paciente) {
				
		Sort example;
		/*example.and
		List<Pacientes> pa = repo.findAll(example, sort)(example);*/
		
	    
	    return null;//userDet;
	}	

}
