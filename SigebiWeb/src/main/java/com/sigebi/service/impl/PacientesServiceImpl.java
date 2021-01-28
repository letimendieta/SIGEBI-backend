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

import com.sigebi.dao.ICarrerasDao;
import com.sigebi.dao.IDepartamentosDao;
import com.sigebi.dao.IDependenciasDao;
import com.sigebi.dao.IEstamentosDao;
import com.sigebi.dao.IPacientesDao;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Carreras;
import com.sigebi.entity.Departamentos;
import com.sigebi.entity.Dependencias;
import com.sigebi.entity.Estamentos;
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
	private IDepartamentosDao departamentosDao;
	
	@Autowired
	private IDependenciasDao dependenciasDao;
	
	@Autowired
	private ICarrerasDao carrerasDao;
	
	@Autowired
	private IEstamentosDao estamentosDao;
	
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

	@Transactional
	public Pacientes guardar(Pacientes paciente) throws Exception {
				
		if( paciente.getPersonas() != null ) {
			//Buscar si la persona ya es paciente
			List<Pacientes> pacienteDb = pacientesDao.findByPersonas(paciente.getPersonas());
			
			if( !pacienteDb.isEmpty() ) {
				throw new Exception("La persona ya existe como paciente");
			}
			
			if( paciente.getPersonas().getPersonaId() != null ) {
				//Busca a la persona y si existe actualizar sus datos
				Personas persona = personasService.findById(paciente.getPersonas().getPersonaId());
				
				if(persona == null) {
					throw new Exception("No se encontro persona con id: " + paciente.getPersonas().getPersonaId());
				}
								
				persona.setNombres(paciente.getPersonas().getNombres());
				persona.setApellidos(paciente.getPersonas().getApellidos());
				persona.setCarreras(paciente.getPersonas().getCarreras());
				persona.setCedula(paciente.getPersonas().getCedula());
				persona.setCelular(paciente.getPersonas().getCelular());
				persona.setDepartamentos(paciente.getPersonas().getDepartamentos());
				persona.setDependencias(paciente.getPersonas().getDependencias());
				persona.setDireccion(paciente.getPersonas().getDireccion());
				persona.setEmail(paciente.getPersonas().getEmail());
				persona.setEstadoCivil(paciente.getPersonas().getEstadoCivil());
				persona.setEstamentos(paciente.getPersonas().getEstamentos());
				persona.setFechaNacimiento(paciente.getPersonas().getFechaNacimiento());
				persona.setNacionalidad(paciente.getPersonas().getNacionalidad());
				persona.setSexo(paciente.getPersonas().getSexo());
				persona.setTelefono(paciente.getPersonas().getTelefono());
				persona.setObservacion(paciente.getPersonas().getObservacion());
				persona.setUsuarioModificacion(paciente.getPersonas().getUsuarioModificacion());
				
				paciente.setPersonas(persona);
				personasService.save(persona);
			}else {
				//Crear nueva persona
				Personas personaNew = personasDao.save(paciente.getPersonas());
				paciente.setPersonas(personaNew);
			}
		}
		
		return pacientesDao.save(paciente);
	}
	
	@Transactional
	public Pacientes actualizar(Pacientes paciente) throws Exception {
				
		//Busca a la persona y actualizar sus datos
		Personas persona = personasService.findById(paciente.getPersonas().getPersonaId());
		
		if(persona == null) {
			throw new Exception("No se encontro persona con id: " + paciente.getPersonas().getPersonaId());
		}
				
		persona.setNombres(paciente.getPersonas().getNombres());
		persona.setApellidos(paciente.getPersonas().getApellidos());
		persona.setCarreras(paciente.getPersonas().getCarreras());
		persona.setCedula(paciente.getPersonas().getCedula());
		persona.setCelular(paciente.getPersonas().getCelular());
		persona.setDepartamentos(paciente.getPersonas().getDepartamentos());
		persona.setDependencias(paciente.getPersonas().getDependencias());
		persona.setDireccion(paciente.getPersonas().getDireccion());
		persona.setEmail(paciente.getPersonas().getEmail());
		persona.setEstadoCivil(paciente.getPersonas().getEstadoCivil());
		persona.setEstamentos(paciente.getPersonas().getEstamentos());
		persona.setFechaNacimiento(paciente.getPersonas().getFechaNacimiento());
		persona.setNacionalidad(paciente.getPersonas().getNacionalidad());
		persona.setSexo(paciente.getPersonas().getSexo());
		persona.setTelefono(paciente.getPersonas().getTelefono());
		persona.setObservacion(paciente.getPersonas().getObservacion());
		persona.setUsuarioModificacion(paciente.getPersonas().getUsuarioModificacion());
		
		paciente.setPersonas(persona);
				
		return pacientesDao.save(paciente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		pacientesDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Pacientes> buscar(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId, Pageable pageable) {
		List<Pacientes> pacientesList = pacientesDao.findAll((Specification<Pacientes>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( paciente.getPacienteId() != null ) {
                p = cb.and(p, cb.equal(root.get("pacienteId"), paciente.getPacienteId()) );
            }
            if ( paciente.getHistorialClinico() != null && paciente.getHistorialClinico().getHistorialClinicoId() != null ) {
                p = cb.and(p, cb.equal(root.get("historialClinico"), paciente.getHistorialClinico().getHistorialClinicoId()) );
            }
            cq.orderBy(cb.desc(root.get("pacienteId")));
            return p;
        }, pageable).getContent();
						
        return pacientesList;
        
    }
	
	@Override
	@Transactional
	public List<Pacientes> buscarNoPaginable(Date fromDate, Date toDate, Pacientes paciente, List<Integer> personasId) {
		List<Pacientes> pacientesList = pacientesDao.findAll((Specification<Pacientes>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( paciente.getPacienteId() != null ) {
                p = cb.and(p, cb.equal(root.get("pacienteId"), paciente.getPacienteId()) );
            }
            if ( paciente.getHistorialClinico() != null && paciente.getHistorialClinico().getHistorialClinicoId() != null ) {
                p = cb.and(p, cb.equal(root.get("historialClinico"), paciente.getHistorialClinico().getHistorialClinicoId()) );
            }
            cq.orderBy(cb.desc(root.get("pacienteId")));
            return p;
        });
        return pacientesList;
    }
	
}
