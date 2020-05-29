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

import com.sigebi.dao.IFuncionariosDao;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PersonasService;

@Service
public class FuncionariosServiceImpl implements FuncionariosService{
	
	@Autowired
	private IFuncionariosDao funcionariosDao;
	
	@Autowired
	private IPersonasDao personasDao;
	
	@Autowired
	private PersonasService personasService;
	
	public FuncionariosServiceImpl(IFuncionariosDao funcionariosDao) {
        this.funcionariosDao = funcionariosDao;
    }
	@Override
	@Transactional(readOnly = true)
	public List<Funcionarios> findAll() {
		return (List<Funcionarios>) funcionariosDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Funcionarios findById(int id) {
		return funcionariosDao.findById(id).orElse(null);
	}

	@Transactional
	public Funcionarios guardar(Funcionarios funcionario) throws Exception {
		
		if( funcionario.getPersonas() != null ) {
			//Buscar si la persona ya es funcionario
			List<Funcionarios> funcionarioDb = funcionariosDao.findByPersonas(funcionario.getPersonas());
			
			if( !funcionarioDb.isEmpty() ) {
				throw new Exception("La persona ya existe como funcionario");
			}
			
			if( funcionario.getPersonas().getPersonaId() != null ) {
				//Busca a la persona y si existe actualizar sus datos
				Personas persona = personasService.findById(funcionario.getPersonas().getPersonaId());
				
				if(persona == null) {
					throw new Exception("No se encontro persona con id: " + funcionario.getPersonas().getPersonaId());
				}
				
				persona.setNombres(funcionario.getPersonas().getNombres());
				persona.setApellidos(funcionario.getPersonas().getApellidos());
				persona.setCarreraId(funcionario.getPersonas().getCarreraId());
				persona.setCedula(funcionario.getPersonas().getCedula());
				persona.setCelular(funcionario.getPersonas().getCelular());
				persona.setDepartamentoId(funcionario.getPersonas().getDepartamentoId());
				persona.setDependenciaId(funcionario.getPersonas().getDependenciaId());
				persona.setDireccion(funcionario.getPersonas().getDireccion());
				persona.setEdad(funcionario.getPersonas().getEdad());
				persona.setEmail(funcionario.getPersonas().getEmail());
				persona.setEstadoCivil(funcionario.getPersonas().getEstadoCivil());
				persona.setEstamentoId(funcionario.getPersonas().getEstamentoId());
				persona.setFechaNacimiento(funcionario.getPersonas().getFechaNacimiento());
				persona.setNacionalidad(funcionario.getPersonas().getNacionalidad());
				persona.setSexo(funcionario.getPersonas().getSexo());
				persona.setTelefono(funcionario.getPersonas().getTelefono());
				persona.setUsuarioModificacion(funcionario.getPersonas().getUsuarioModificacion());
				
				funcionario.setPersonas(persona);
			}else {
				//Crear nueva persona
				Personas personaNew = personasDao.save(funcionario.getPersonas());
				funcionario.setPersonas(personaNew);
			}
		}
		
		return funcionariosDao.save(funcionario);
	}
	
	@Transactional
	public Funcionarios actualizar(Funcionarios funcionario) throws Exception {
						
		//Busca a la persona y actualizar sus datos
		Personas persona = personasService.findById(funcionario.getPersonas().getPersonaId());
		
		if(persona == null) {
			throw new Exception("No se encontro persona con id: " + funcionario.getPersonas().getPersonaId());
		}
		
		persona.setNombres(funcionario.getPersonas().getNombres());
		persona.setApellidos(funcionario.getPersonas().getApellidos());
		persona.setCarreraId(funcionario.getPersonas().getCarreraId());
		persona.setCedula(funcionario.getPersonas().getCedula());
		persona.setCelular(funcionario.getPersonas().getCelular());
		persona.setDepartamentoId(funcionario.getPersonas().getDepartamentoId());
		persona.setDependenciaId(funcionario.getPersonas().getDependenciaId());
		persona.setDireccion(funcionario.getPersonas().getDireccion());
		persona.setEdad(funcionario.getPersonas().getEdad());
		persona.setEmail(funcionario.getPersonas().getEmail());
		persona.setEstadoCivil(funcionario.getPersonas().getEstadoCivil());
		persona.setEstamentoId(funcionario.getPersonas().getEstamentoId());
		persona.setFechaNacimiento(funcionario.getPersonas().getFechaNacimiento());
		persona.setNacionalidad(funcionario.getPersonas().getNacionalidad());
		persona.setSexo(funcionario.getPersonas().getSexo());
		persona.setTelefono(funcionario.getPersonas().getTelefono());
		persona.setUsuarioModificacion(funcionario.getPersonas().getUsuarioModificacion());
		
		funcionario.setPersonas(persona);
				
		return funcionariosDao.save(funcionario);
	}

	@Override
	@Transactional
	public void delete(int id) {
		funcionariosDao.deleteById(id);
	}
	
	@Override
	//@Transactional(readOnly = true)
	public List<Funcionarios> buscar(Date fromDate, Date toDate, Funcionarios funcionario, List<Integer> funcionariosId, Pageable pageable) {
		List<Funcionarios> funcionariosList = funcionariosDao.findAll((Specification<Funcionarios>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( funcionariosId != null && !funcionariosId.isEmpty() ){
            	p = cb.and(root.get("personas").in(funcionariosId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( funcionario.getFuncionarioId() != null ) {
                p = cb.and(p, cb.equal(root.get("funcionarioId"), funcionario.getFuncionarioId()) );
            }
            cq.orderBy(cb.desc(root.get("funcionarioId")));
            return p;
        }, pageable).getContent();
        return funcionariosList;
    }
}
