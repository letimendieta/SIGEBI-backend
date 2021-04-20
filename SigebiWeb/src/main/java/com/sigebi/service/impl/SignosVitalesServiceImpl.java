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

import com.sigebi.dao.ISignosVitalesDao;
import com.sigebi.entity.SignosVitales;
import com.sigebi.service.SignosVitalesService;


@Service
public class SignosVitalesServiceImpl implements SignosVitalesService{

	@Autowired
	private ISignosVitalesDao signoVitalsDao;
	
	public SignosVitalesServiceImpl(ISignosVitalesDao signoVitalsDao) {
        this.signoVitalsDao = signoVitalsDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<SignosVitales> findAll() {
		return (List<SignosVitales>) signoVitalsDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public SignosVitales findById(int id) {
		return signoVitalsDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public SignosVitales save(SignosVitales signoVital) {
		return signoVitalsDao.save(signoVital);
	}

	@Override
	@Transactional
	public void delete(int id) {
		signoVitalsDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<SignosVitales> buscar(Date fromDate, Date toDate, SignosVitales signoVital, String orderBy, String orderDir, Pageable pageable) {
		List<SignosVitales> signoVitalsList;
		
		Specification<SignosVitales> signoVitalsSpec = (Specification<SignosVitales>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if ( signoVital.getFecha() != null ) {
                p = cb.and(p, cb.equal( root.get("fecha"), signoVital.getFecha()) );
            }
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( signoVital.getSignoVitalId() != null ) {
                p = cb.and(p, cb.equal(root.get("signoVitalId"), signoVital.getSignoVitalId()) );
            }
            if( signoVital.getPacientes() != null && signoVital.getPacientes().getPacienteId() != null ){
            	p = cb.and(p, cb.equal(root.get("pacientes"), signoVital.getPacientes().getPacienteId()) );
            }
                        
            String orden = "signoVitalId";
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
			signoVitalsList = signoVitalsDao.findAll(signoVitalsSpec, pageable).getContent();			
		}else {
			signoVitalsList = signoVitalsDao.findAll(signoVitalsSpec);
		}
        
        return signoVitalsList;
    }

}
