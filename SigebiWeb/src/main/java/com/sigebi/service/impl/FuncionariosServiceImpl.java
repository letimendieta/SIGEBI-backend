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

import com.sigebi.dao.IFuncionariosDao;
import com.sigebi.dao.IPersonasDao;
import com.sigebi.entity.Carreras;
import com.sigebi.entity.Departamentos;
import com.sigebi.entity.Dependencias;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.entity.Estamentos;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PersonasService;
import com.sigebi.util.exceptions.SigebiException;

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
	public int count() {
		return (int) funcionariosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Funcionarios findById(int id) {
		return funcionariosDao.findById(id).orElse(null);
	}

	@Transactional
	public Funcionarios guardar(Funcionarios funcionario) throws Exception {
		Personas persona = null;
		
		if ( funcionario.getPersonas() == null ) {
			throw new SigebiException.BusinessException("Datos de la persona es requerido ");
		}
		
		if( funcionario.getPersonas() != null ) {
			//Buscar si la persona ya es funcionario
			List<Funcionarios> funcionarioDb = funcionariosDao.findByPersonas(funcionario.getPersonas());
			
			if( !funcionarioDb.isEmpty() ) {
				throw new Exception("La persona ya existe como funcionario");
			}
			
			if( funcionario.getPersonas().getPersonaId() != null ) {
				//Busca a la persona y si existe actualizar sus datos
				persona = personasService.obtener(funcionario.getPersonas().getPersonaId());
				
				if(persona == null) {
					throw new Exception("No se encontró persona con id: " + funcionario.getPersonas().getPersonaId());
				}				
				if( persona.getDepartamentos() != null &&  persona.getDepartamentos().getDepartamentoId() == null) {
					persona.setDepartamentos(null);
				}
				if( persona.getDependencias() != null &&  persona.getDependencias().getDependenciaId() == null) {
					persona.setDependencias(null);
				}
				if( persona.getCarreras() != null &&  persona.getCarreras().getCarreraId() == null) {
					persona.setCarreras(null);
				}
				if( persona.getEstamentos() != null &&  persona.getEstamentos().getEstamentoId() == null) {
					persona.setEstamentos(null);
				}				
			}else {
				throw new Exception("Id de la persona es requerido ");
			}				
		}
		
		funcionario.setPersonas(persona);
		
		return funcionariosDao.save(funcionario);
	}
	
	@Transactional
	public Funcionarios actualizar(Funcionarios funcionario) throws Exception {
						
		//Busca a la persona y actualizar sus datos
		Personas persona = personasDao.findById(funcionario.getPersonas().getPersonaId()).orElse(null); //personasDao.findById(funcionario.getPersonas().getPersonaId());// personasService.obtener(funcionario.getPersonas().getPersonaId());
		
		if(persona == null) {
			throw new Exception("No se encontro persona con id: " + funcionario.getPersonas().getPersonaId());
		}
		
		funcionario.setPersonas(persona);
				
		return funcionariosDao.save(funcionario);
	}

	@Override
	@Transactional
	public void delete(int id) {
		funcionariosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Funcionarios> buscar(Date fromDate, Date toDate, Funcionarios funcionario, List<Integer> personasId, String orderBy, String orderDir, Pageable pageable){
		List<Funcionarios> funcionariosList;
		
		Specification<Funcionarios> funcionarioSpec = (Specification<Funcionarios>) (root, cq, cb) -> {
		            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( funcionario != null && funcionario.getFuncionarioId() != null ) {
                p = cb.and(p, cb.equal(root.get("funcionarioId"), funcionario.getFuncionarioId()) );
            }
            if ( funcionario != null && funcionario.getAreas() != null && funcionario.getAreas().getAreaId() != null ) {
                p = cb.and(p, cb.equal(root.get("areas"), funcionario.getAreas().getAreaId()) );
            }
            if ( funcionario != null && funcionario.getEstado() != null ) {
                p = cb.and(p, cb.equal(root.get("estado"), funcionario.getEstado() ));
            }
            String orden = "funcionarioId";
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
        if(pageable != null) {
        	funcionariosList = funcionariosDao.findAll(funcionarioSpec, pageable).getContent();			
		}else {
			funcionariosList = funcionariosDao.findAll(funcionarioSpec);
		}
        return funcionariosList;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Funcionarios> buscarNoPaginable(Date fromDate, Date toDate, Funcionarios funcionario, List<Integer> personasId){
		List<Funcionarios> funcionariosList = funcionariosDao.findAll((Specification<Funcionarios>) (root, cq, cb) -> {
		
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
            }            
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( funcionario != null && funcionario.getFuncionarioId() != null ) {
                p = cb.and(p, cb.equal(root.get("funcionarioId"), funcionario.getFuncionarioId()) );
            }
            if ( funcionario != null && funcionario.getAreas() != null && funcionario.getAreas().getAreaId() != null ) {
                p = cb.and(p, cb.equal(root.get("areas"), funcionario.getAreas().getAreaId()) );
            }
            if ( funcionario != null && funcionario.getEstado() != null ) {
                p = cb.and(p, cb.equal(root.get("estado"), funcionario.getEstado() ));
            }          
            
            cq.orderBy(cb.desc(root.get("funcionarioId")));
            return p;
		});
        return funcionariosList;
    }
}
