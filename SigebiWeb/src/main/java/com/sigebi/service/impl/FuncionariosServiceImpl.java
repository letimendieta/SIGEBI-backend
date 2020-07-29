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
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.Personas;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.PersonasService;

@Service
public class FuncionariosServiceImpl implements FuncionariosService{
	
	@Autowired
	private IFuncionariosDao funcionariosDao;
	
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
			}else {
				throw new Exception("Id de la persona es requerido ");
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
				
		return funcionariosDao.save(funcionario);
	}

	@Override
	@Transactional
	public void delete(int id) {
		funcionariosDao.deleteById(id);
	}
	
	@Override
	public List<Funcionarios> buscar(Date fromDate, Date toDate, Funcionarios funcionario, List<Integer> personasId, Pageable pageable) {
		List<Funcionarios> funcionariosList = funcionariosDao.findAll((Specification<Funcionarios>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( personasId != null && !personasId.isEmpty() ){
            	p = cb.and(root.get("personas").in(personasId));
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
