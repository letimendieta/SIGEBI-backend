package com.sigebi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Personas;
import com.sigebi.service.PersonasService;


@Service
public class PersonasServiceImpl implements PersonasService{

	@Autowired
	private IPersonasDao personasDao;
	
	public PersonasServiceImpl(IPersonasDao personasDao) {
        this.personasDao = personasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Personas> findAll() {
		return (List<Personas>) personasDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Personas findById(int id) {
		return personasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Personas save(Personas cliente) {
		return personasDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		personasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Personas> buscar(Date fromDate, Date toDate, Personas persona, Pageable pageable) {
		
        List<Personas> personasList = personasDao.findAll((Specification<Personas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            /*if ( persona.getPersonaId() != 0) {
                p = cb.and(p, cb.equals(root.get("personaId"), persona.getPersonaId()));
            }*/
            if (!StringUtils.isEmpty(persona.getCedula())) {
                p = cb.and(p, cb.like(root.get("cedula"), "%" + persona.getCedula() + "%"));
            }
            if (!StringUtils.isEmpty(persona.getNombres())) {
                p = cb.and(p, cb.like(root.get("nombres"), "%" + persona.getNombres() + "%"));
            }
            if (!StringUtils.isEmpty(persona.getApellidos())) {
                p = cb.and(p, cb.like(root.get("apellidos"), "%" + persona.getApellidos() + "%"));
            }
            cq.orderBy(cb.desc(root.get("personaId")));
            return p;
        }, pageable).getContent();
        return personasList;
    }

}
