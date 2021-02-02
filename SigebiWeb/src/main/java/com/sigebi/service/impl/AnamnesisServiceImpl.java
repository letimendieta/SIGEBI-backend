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

import com.sigebi.dao.IAnamnesisDao;
import com.sigebi.entity.Anamnesis;
import com.sigebi.service.AnamnesisService;


@Service
public class AnamnesisServiceImpl implements AnamnesisService{

	@Autowired
	private IAnamnesisDao anamnesisDao;
	
	public AnamnesisServiceImpl(IAnamnesisDao anamnesisDao) {
        this.anamnesisDao = anamnesisDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Anamnesis> findAll() {
		return (List<Anamnesis>) anamnesisDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) anamnesisDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Anamnesis findById(int id) {
		return anamnesisDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Anamnesis save(Anamnesis cliente) {
		return anamnesisDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		anamnesisDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Anamnesis> buscar(Date fromDate, Date toDate, Anamnesis anamnesis, String orderBy, String orderDir, Pageable pageable) {
		List<Anamnesis> anamnesisList;
		
		Specification<Anamnesis> anamnesisSpec = (Specification<Anamnesis>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( anamnesis.getAnamnesisId() != null ) {
                p = cb.and(p, cb.equal(root.get("anamnesisId"), anamnesis.getAnamnesisId()) );
            }
            if( anamnesis.getPacienteId() != null ){
            	p = cb.and(p, cb.equal(root.get("pacienteId"), anamnesis.getPacienteId()) );
            } 
                        
            String orden = "anamnesisId";
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
			anamnesisList = anamnesisDao.findAll(anamnesisSpec, pageable).getContent();			
		}else {
			anamnesisList = anamnesisDao.findAll(anamnesisSpec);
		}
        
        return anamnesisList;
    }

}
