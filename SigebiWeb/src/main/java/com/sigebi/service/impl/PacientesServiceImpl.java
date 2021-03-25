package com.sigebi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.clases.FichaMedica;
import com.sigebi.clases.Globales;
import com.sigebi.clases.ProcesoPacienteFichaClinica;
import com.sigebi.dao.IAlergiasDao;
import com.sigebi.dao.IAntecedentesDao;
import com.sigebi.dao.IPacientesDao;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.dao.IVacunacionesDao;
import com.sigebi.dao.IVacunasDao;
import com.sigebi.entity.Alergenos;
import com.sigebi.entity.Alergias;
import com.sigebi.entity.Antecedentes;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.PatologiasProcedimientos;
import com.sigebi.entity.Personas;
import com.sigebi.entity.Vacunaciones;
import com.sigebi.entity.Vacunas;
import com.sigebi.service.AlergenosService;
import com.sigebi.service.PacientesService;
import com.sigebi.service.PatologiasProcedimientosService;
import com.sigebi.service.PersonasService;
import com.sigebi.service.VacunasService;


@Service
public class PacientesServiceImpl implements PacientesService{

	@Autowired
	private IPacientesDao pacientesDao;	
	@Autowired
	private IPersonasDao personasDao;	
	@Autowired
	private PersonasService personasService;	
	@Autowired
	private AlergenosService alergenosService;
	@Autowired
	private IAlergiasDao alergiasDao;
	@Autowired
	private PatologiasProcedimientosService patologiasProcedimientosService;
	@Autowired
	private IAntecedentesDao antecedentesDao;
	@Autowired
	private VacunasService vacunasService;
	@Autowired
	private IVacunacionesDao vacunacionesDao;
	
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
			
			List<Personas> personaDb = personasDao.findByCedula(paciente.getPersonas().getCedula());
			if( personaDb != null && !personaDb.isEmpty() ) {
				List<Integer> pacienteIds = new ArrayList<Integer>();
				pacienteIds.add(personaDb.get(0).getPersonaId());
				
				List<Pacientes> pacienteDb = buscarNoPaginable(null, null, new Pacientes(), pacienteIds);
				
				if( !pacienteDb.isEmpty() ) {
					throw new Exception("La persona ya existe como paciente");
				}
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
	public Pacientes guardarPacienteFichaClinica(ProcesoPacienteFichaClinica pacienteFichaClinica) throws Exception {
				
		Pacientes paciente = guardar(pacienteFichaClinica.getPaciente());
		
		//Insertar la ficha clinica	
		try {
			for(Integer alergenoId : pacienteFichaClinica.getAlergenosIdList()) {
				Alergias alergia = new Alergias();
				
				Alergenos alergeno = alergenosService.findById(alergenoId);
				
				alergia.setAlergenos(alergeno);
				alergia.setUsuarioCreacion(pacienteFichaClinica.getPaciente().getUsuarioCreacion());
				alergia.setPacienteId(paciente.getPacienteId());
				
				alergiasDao.save(alergia);				
			}
			
			for(Integer patologiaProcedimientoId : pacienteFichaClinica.getPatologiasProcedimientosIdList()) {
				Antecedentes antecedentes = new Antecedentes();
				PatologiasProcedimientos patologiaProcedimiento = new PatologiasProcedimientos();
				
				patologiaProcedimiento = patologiasProcedimientosService.findById(patologiaProcedimientoId);
				
				antecedentes.setPatologiasProcedimientos(patologiaProcedimiento);
				antecedentes.setPacienteId(paciente.getPacienteId());
				antecedentes.setUsuarioCreacion(pacienteFichaClinica.getPaciente().getUsuarioCreacion());
				antecedentes.setTipo(Globales.TiposAntecedentes.PERSONAL);
				antecedentesDao.save(antecedentes);				
			}
			
			for(Integer patologiaFamiliarId : pacienteFichaClinica.getPatologiasFamiliaresIdList()) {
				Antecedentes antecedentes = new Antecedentes();
				PatologiasProcedimientos patologiaProcedimiento = new PatologiasProcedimientos();
				
				patologiaProcedimiento = patologiasProcedimientosService.findById(patologiaFamiliarId);
				
				antecedentes.setPatologiasProcedimientos(patologiaProcedimiento);
				antecedentes.setPacienteId(paciente.getPacienteId());
				antecedentes.setUsuarioCreacion(pacienteFichaClinica.getPaciente().getUsuarioCreacion());
				antecedentes.setTipo(Globales.TiposAntecedentes.FAMILIAR);
				antecedentesDao.save(antecedentes);				
			}
			
			for(Integer vacunaId : pacienteFichaClinica.getVacunasIdList()) {
				Vacunaciones vacunaciones = new Vacunaciones();
				Vacunas vacuna = new Vacunas();
				
				vacuna = vacunasService.findById(vacunaId);
				
				vacunaciones.setVacunas(vacuna);
				vacunaciones.setPacienteId(paciente.getPacienteId());
				vacunaciones.setUsuarioCreacion(pacienteFichaClinica.getPaciente().getUsuarioCreacion());
				vacunacionesDao.save(vacunaciones);				
			}
			
		} catch (Exception e) {
			//throw new Exception("Error al insertar la ficha del paciente " + e.getMessage());
		}
		
		return paciente;
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
         
            cq.orderBy(cb.desc(root.get("pacienteId")));
            return p;
        });
        return pacientesList;
    }
	
}
