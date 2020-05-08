package com.sigebi.serviceImplement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sigebi.model.Pacientes;
import com.sigebi.service.IPacientesRepo;

@Service
public class PacientesService{

	@Autowired
	private IPacientesRepo repo;
	
		
	public List<Pacientes> obtenerPaciente(Pacientes paciente) {
				
		Sort example;
		/*example.and
		List<Pacientes> pa = repo.findAll(example, sort)(example);*/
		
	    
	    return null;//userDet;
	}	

}
