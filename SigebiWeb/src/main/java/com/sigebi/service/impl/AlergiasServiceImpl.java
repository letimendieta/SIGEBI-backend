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

import com.sigebi.dao.IAlergiasDao;
import com.sigebi.entity.Alergias;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.service.AlergiasService;


@Service
public class AlergiasServiceImpl implements AlergiasService{

	@Autowired
	private IAlergiasDao alergiasDao;
	
	public AlergiasServiceImpl(IAlergiasDao alergiasDao) {
        this.alergiasDao = alergiasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Alergias> findAll() {
		return (List<Alergias>) alergiasDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) alergiasDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Alergias findById(int id) {
		return alergiasDao.findById(id).orElse(null);
	}

	@Transactional
	public Alergias guardar(Alergias alergias) throws Exception {
						
		return alergiasDao.save(alergias);
	}
	
	@Transactional
	public Alergias actualizar(Alergias alergias) throws Exception {
						
		return alergiasDao.save(alergias);
	}

	@Override
	@Transactional
	public void delete(int id) {
		alergiasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Alergias> buscar(Date fromDate, Date toDate, Alergias alergias, String orderBy, String orderDir, Pageable pageable) {
		List<Alergias> AlergiasList;
		
		Specification<Alergias> enfermedadesCie10Spec = (Specification<Alergias>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();            
            if( alergias.getPacienteId() != null ){
            	p = cb.and(p, cb.equal(root.get("pacienteId"), alergias.getPacienteId()) );
            } 
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( alergias.getAlergiaId() != null ) {
                p = cb.and(p, cb.equal(root.get("alergiaId"), alergias.getAlergiaId()) );
            }
           
            String orden = "alergiaId";
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
        	AlergiasList = alergiasDao.findAll(enfermedadesCie10Spec, pageable).getContent();			
		}else {
			AlergiasList = alergiasDao.findAll(enfermedadesCie10Spec);
		}
        return AlergiasList;
    }
	
}
