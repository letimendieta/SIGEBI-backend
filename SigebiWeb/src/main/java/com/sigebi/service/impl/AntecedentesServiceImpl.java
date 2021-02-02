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

import com.sigebi.dao.IAntecedentesDao;
import com.sigebi.entity.Antecedentes;
import com.sigebi.service.AntecedentesService;


@Service
public class AntecedentesServiceImpl implements AntecedentesService{

	@Autowired
	private IAntecedentesDao antecedentesDao;
	
	public AntecedentesServiceImpl(IAntecedentesDao antecedentesDao) {
        this.antecedentesDao = antecedentesDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Antecedentes> findAll() {
		return (List<Antecedentes>) antecedentesDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Antecedentes findById(int id) {
		return antecedentesDao.findById(id).orElse(null);
	}

	@Transactional
	public Antecedentes guardar(Antecedentes antecedentes) throws Exception {
						
		return antecedentesDao.save(antecedentes);
	}
	
	@Transactional
	public Antecedentes actualizar(Antecedentes antecedentes) throws Exception {
						
		return antecedentesDao.save(antecedentes);
	}

	@Override
	@Transactional
	public void delete(int id) {
		antecedentesDao.deleteById(id);
	}
	
	@Override
	public List<Antecedentes> buscar(Date fromDate, Date toDate, Antecedentes antecedentes, 
										Pageable pageable) {
		List<Antecedentes> AntecedentesList = antecedentesDao.findAll((Specification<Antecedentes>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();            
            if( antecedentes.getPacienteId() != null ){
            	p = cb.and(p, cb.equal(root.get("pacienteId"), antecedentes.getPacienteId()) );
            } 
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( antecedentes.getAntecedenteId() != null ) {
                p = cb.and(p, cb.equal(root.get("antecedenteId"), antecedentes.getAntecedenteId()) );
            }
            if ( antecedentes.getTipo() != null ) {
                p = cb.and(p, cb.equal(root.get("tipo"), antecedentes.getTipo()) );
            }
           
            cq.orderBy(cb.desc(root.get("antecedenteId")));
            return p;
        }, pageable).getContent();
        return AntecedentesList;
    }
	
}
