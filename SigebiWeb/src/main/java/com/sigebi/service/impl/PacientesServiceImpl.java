package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.dao.IPacientesDao;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.service.PacientesService;
import com.sigebi.service.PersonasService;


@Service
public class PacientesServiceImpl implements PacientesService{

	@Autowired
	private IPacientesDao pacientesDao;
	
	@Autowired
	private IPersonasDao personasDao;
	
	@Autowired
	private PersonasService personasService;
	
	public PacientesServiceImpl(IPacientesDao pacientesDao) {
        this.pacientesDao = pacientesDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Pacientes> findAll() {
		return (List<Pacientes>) pacientesDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Pacientes findById(int id) {
		return pacientesDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Pacientes save(Pacientes paciente) {
		return pacientesDao.save(paciente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		pacientesDao.deleteById(id);
	}
	
	@Override
	//@Transactional(readOnly = true)
	public List<Pacientes> buscar(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId, Pageable pageable) {
		List<Pacientes> pacientesList = pacientesDao.findAll((Specification<Pacientes>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            //Root<Personas> personaRoot= cq.from(Personas.class);
            //root.join("personas");
            //cq.select
            //cq.where(cb.and(cb.equal(root.get(root_.), y)))\cq
            //cq.           
            
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( paciente.getPacienteId() != null ) {
                p = cb.and(p, cb.equal(root.get("pacienteId"), paciente.getPacienteId()) );
            }
            /*if( paciente.getPersonas() != null) {
	            if ( paciente.getPersonas().getPersonaId() != null ) {
	                p = cb.and(p, cb.equal(root.get("personas"),paciente.getPersonas().getPersonaId()));
	            }
        	}*/
            cq.orderBy(cb.desc(root.get("pacienteId")));
            return p;
        }, pageable).getContent();
        return pacientesList;
    }
	
	@Override
	//@Transactional(readOnly = true)
	public List<Pacientes> busqueda(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId, Pageable pageable){
		
		/*List<Integer> personasIds = new ArrayList<Integer>();
		List<Personas> personasList = new ArrayList<Personas>();
		if( paciente.getPersonas() != null) {
			try {
				personasList = personasService.buscar(null, null, paciente.getPersonas(), null);
				//personasList = personasDao.findByNombres(paciente.getPersonas().getNombres());
			} catch (Exception e) {
				// TODO: handle exception
			}	
			
			for( Personas persona : personasList ){
				personasIds.add(persona.getPersonaId());
			}
		}*/
		List<Pacientes> pacientesList = new ArrayList<Pacientes>();
		try {
			pacientesList = this.buscar(fromDate, toDate, paciente, personasId, pageable);
		} catch (Exception e) {
			// TODO: handle exception
		}	
		
        return pacientesList;
    }
	
}
