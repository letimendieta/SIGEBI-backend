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

import com.sigebi.dao.ICarrerasDao;
import com.sigebi.dao.IDepartamentosDao;
import com.sigebi.dao.IDependenciasDao;
import com.sigebi.dao.IEstamentosDao;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Carreras;
import com.sigebi.entity.Departamentos;
import com.sigebi.entity.Dependencias;
import com.sigebi.entity.Estamentos;
import com.sigebi.entity.Personas;
import com.sigebi.service.CarrerasService;
import com.sigebi.service.DepartamentosService;
import com.sigebi.service.DependenciasService;
import com.sigebi.service.EstamentosService;
import com.sigebi.service.PersonasService;


@Service
public class PersonasServiceImpl implements PersonasService{

	@Autowired
	private IPersonasDao personasDao;
	@Autowired
	private CarrerasService carrerasService;
	@Autowired
	private DepartamentosService departamentosService;
	@Autowired
	private DependenciasService dependenciasService;
	@Autowired
	private EstamentosService estamentosService;
	
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
	public Personas save(Personas persona) throws Exception {
		if( persona.getCarreras() != null) {
			Carreras carrera = carrerasService.findById(persona.getCarreras().getCarreraId());
			if(carrera == null) {
				throw new Exception("No se encontro carrera con id: " + persona.getCarreras().getCarreraId());
			}
			persona.setCarreras(carrera);
		}
		if( persona.getDepartamentos() != null) {
			Departamentos departamento = departamentosService.findById(persona.getDepartamentos().getDepartamentoId());
			if(departamento == null) {
				throw new Exception("No se encontro departamento con id: " + persona.getDepartamentos().getDepartamentoId());
			}
			persona.setDepartamentos(departamento);
		}
		if( persona.getDependencias() != null) {
			Dependencias dependencia = dependenciasService.findById(persona.getDependencias().getDependenciaId());
			if(dependencia == null) {
				throw new Exception("No se encontro dependencia con id: " + persona.getDependencias().getDependenciaId());
			}
			persona.setDependencias(dependencia);
		}
		if( persona.getEstamentos() != null) {
			Estamentos estamento = estamentosService.findById(persona.getEstamentos().getEstamentoId());
			if(estamento == null) {
				throw new Exception("No se encontro estamento con id: " + persona.getEstamentos().getEstamentoId());
			}
			persona.setEstamentos(estamento);
		}
		return personasDao.save(persona);
	}

	@Override
	@Transactional
	public void delete(int id) {
		personasDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Personas> buscar(Date fromDate, Date toDate, Personas persona, Pageable pageable) {
		
        List<Personas> personasList = personasDao.findAll((Specification<Personas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( persona.getPersonaId() != null ) {
                p = cb.and(p, cb.equal(root.get("personaId"), persona.getPersonaId()) );
            }
            if (!StringUtils.isEmpty(persona.getCedula())) {
                p = cb.and(p, cb.equal(root.get("cedula"), persona.getCedula()));
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
