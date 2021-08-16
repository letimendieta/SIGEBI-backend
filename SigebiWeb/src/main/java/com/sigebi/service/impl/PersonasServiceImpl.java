package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Carreras;
import com.sigebi.entity.Departamentos;
import com.sigebi.entity.Dependencias;
import com.sigebi.entity.Estamentos;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.Personas;
import com.sigebi.service.CarrerasService;
import com.sigebi.service.DepartamentosService;
import com.sigebi.service.DependenciasService;
import com.sigebi.service.EstamentosService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;


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
	@Autowired
	private UtilesService utiles;
	
	public PersonasServiceImpl(IPersonasDao personasDao) {
        this.personasDao = personasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Personas> listar() throws SigebiException {
		List<Personas> personasList = personasDao.findAll();
		
		if( personasList.isEmpty()) {
			throw new SigebiException.DataNotFound("No se encontraron datos");
		}
		
		return personasList;
	}

	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) personasDao.count();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Personas obtener(int id) throws SigebiException {
		
		Personas persona = personasDao.findById(id).orElse(null);
		
		if ( persona != null ) {
			if( persona.getDepartamentos() == null ) {
				persona.setDepartamentos(new Departamentos());
			}
			if( persona.getDependencias() == null ) {
				persona.setDependencias(new Dependencias());
			}
			if( persona.getCarreras() == null ) {
				persona.setCarreras(new Carreras());
			}
			if( persona.getEstamentos() == null ) {
				persona.setEstamentos(new Estamentos());
			}
		}
		
		if( persona == null ) {						
			String mensaje = "La persona con ID: ".concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		return persona;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Personas obtenerPorCedula(String cedula) throws SigebiException {
		
		Personas persona = personasDao.findByCedula(cedula);
		
		if ( persona != null ) {
			if( persona.getDepartamentos() == null ) {
				persona.setDepartamentos(new Departamentos());
			}
			if( persona.getDependencias() == null ) {
				persona.setDependencias(new Dependencias());
			}
			if( persona.getCarreras() == null ) {
				persona.setCarreras(new Carreras());
			}
			if( persona.getEstamentos() == null ) {
				persona.setEstamentos(new Estamentos());
			}
		}
		
		if( persona == null ) {						
			String mensaje = "La persona con cédula: ".concat(String.valueOf(cedula).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		return persona;
	}

	@Override
	@Transactional
	public Personas guardar(Personas persona) throws SigebiException {
		
		Personas personaDb = personasDao.findByCedula(persona.getCedula());
		
		if( personaDb != null ) {
			throw new SigebiException.DataAlreadyExist("Ya existe una persona con cédula: " + persona.getCedula());
		}
		
		if( persona.getCarreras() != null && persona.getCarreras().getCarreraId() != null) {
			Carreras carrera = carrerasService.findById(persona.getCarreras().getCarreraId());
			if(carrera == null) {
				throw new SigebiException.DataNotFound("No se encontro carrera con id: " + persona.getCarreras().getCarreraId());
			}
			persona.setCarreras(carrera);
		}
		if( persona.getDepartamentos() != null && persona.getDepartamentos().getDepartamentoId() != null) {
			Departamentos departamento = departamentosService.findById(persona.getDepartamentos().getDepartamentoId());
			if(departamento == null) {
				throw new SigebiException.DataNotFound("No se encontro departamento con id: " + persona.getDepartamentos().getDepartamentoId());
			}
			persona.setDepartamentos(departamento);
		}
		if( persona.getDependencias() != null && persona.getDependencias().getDependenciaId() != null) {
			Dependencias dependencia = dependenciasService.findById(persona.getDependencias().getDependenciaId());
			if(dependencia == null) {
				throw new SigebiException.DataNotFound("No se encontro dependencia con id: " + persona.getDependencias().getDependenciaId());
			}
			persona.setDependencias(dependencia);
		}
		if( persona.getEstamentos() != null && persona.getEstamentos().getEstamentoId() != null) {
			Estamentos estamento = estamentosService.findById(persona.getEstamentos().getEstamentoId());
			if(estamento == null) {
				throw new SigebiException.DataNotFound("No se encontro estamento con id: " + persona.getEstamentos().getEstamentoId());
			}
			persona.setEstamentos(estamento);
		}
		return personasDao.save(persona);
	}
	
	@Override
	@Transactional
	public Personas actualizar(Personas persona) throws SigebiException {
		
		if ( persona.getPersonaId() == null ) {
			throw new SigebiException.DataNotFound("Error: persona id es requerido ");
		}
		
		Personas personaActual = obtener(persona.getPersonaId());
		
		if ( personaActual == null ) {
			String mensaje = "No se pudo editar, la persona ID: "
					.concat(String.valueOf(persona.getPersonaId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		if( persona.getCarreras() != null && persona.getCarreras().getCarreraId() != null) {
			Carreras carrera = carrerasService.findById(persona.getCarreras().getCarreraId());
			if(carrera == null) {
				throw new SigebiException.DataNotFound("No se encontro carrera con id: " + persona.getCarreras().getCarreraId());
			}
			persona.setCarreras(carrera);
		}else {
			persona.setCarreras(null);
		}
		
		if( persona.getDepartamentos() != null && persona.getDepartamentos().getDepartamentoId() != null) {
			Departamentos departamento = departamentosService.findById(persona.getDepartamentos().getDepartamentoId());
			if(departamento == null) {
				throw new SigebiException.DataNotFound("No se encontro departamento con id: " + persona.getDepartamentos().getDepartamentoId());
			}
			persona.setDepartamentos(departamento);
		}else {
			persona.setDepartamentos(null);
		}
		
		if( persona.getDependencias() != null && persona.getDependencias().getDependenciaId() != null) {
			Dependencias dependencia = dependenciasService.findById(persona.getDependencias().getDependenciaId());
			if(dependencia == null) {
				throw new SigebiException.DataNotFound("No se encontro dependencia con id: " + persona.getDependencias().getDependenciaId());
			}
			persona.setDependencias(dependencia);
		}else {
			persona.setDependencias(null);
		}
		
		if( persona.getEstamentos() != null && persona.getEstamentos().getEstamentoId() != null) {
			Estamentos estamento = estamentosService.findById(persona.getEstamentos().getEstamentoId());
			if(estamento == null) {
				throw new SigebiException.DataNotFound("No se encontro estamento con id: " + persona.getEstamentos().getEstamentoId());
			}
			persona.setEstamentos(estamento);
		}else {
			persona.setEstamentos(null);
		}
		
		return personasDao.save(persona);
	}

	@Override
	@Transactional
	public void eliminar(int id) throws SigebiException {
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			throw new SigebiException.DataNotFound("Error: persona id es requerido ");
		}
		
		Personas personaActual = obtener(id);
		
		if ( personaActual == null ) {
			String mensaje = "La persona ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		personasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Personas> buscar(Date fromDate, Date toDate, Personas persona, String orderBy, String orderDir, Pageable pageable) {
		
		List<Personas> personasList = new ArrayList<Personas>();
		
		Specification<Personas> personasSpec = (Specification<Personas>) (root, cq, cb) -> {
		
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
                p = cb.and(p, cb.like(cb.lower(root.get("nombres")), "%" + persona.getNombres().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(persona.getApellidos())) {
                p = cb.and(p, cb.like(cb.lower(root.get("apellidos")), "%" + persona.getApellidos().toLowerCase() + "%"));
            }
            String orden = "personaId";
            if (!StringUtils.isEmpty(orderBy)) {
            	orden = orderBy;
            }
            if("asc".equalsIgnoreCase(orderDir)){
            	cq.orderBy(cb.asc(root.get(orden)));
            }else {
            	cq.orderBy(cb.desc(root.get(orden)));
            }
            return p;
        };
        
        for( Personas personaList : personasList) {
			
			if( personaList.getDepartamentos() == null ) {
				personaList.setDepartamentos(new Departamentos());
			}
			if( personaList.getDependencias() == null ) {
				personaList.setDependencias(new Dependencias());
			}
			if( personaList.getCarreras() == null ) {
				personaList.setCarreras(new Carreras());
			}
			if( personaList.getEstamentos() == null ) {
				personaList.setEstamentos(new Estamentos());
			}
		}
        
        if(pageable != null) {
        	personasList = personasDao.findAll(personasSpec, pageable).getContent();			
		}else {
			personasList = personasDao.findAll(personasSpec);
		}
        
        return personasList;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Personas> buscarNoPaginable(Date fromDate, Date toDate, Personas persona) {
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
                p = cb.and(p, cb.like(cb.lower(root.get("nombres")), "%" + persona.getNombres().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(persona.getApellidos())) {
                p = cb.and(p, cb.like(cb.lower(root.get("apellidos")), "%" + persona.getApellidos().toLowerCase() + "%"));
            }
         
            cq.orderBy(cb.desc(root.get("personaId")));
            return p;
        });
        return personasList;
    }

}
