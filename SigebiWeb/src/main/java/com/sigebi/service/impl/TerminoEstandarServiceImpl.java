package com.sigebi.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.dao.ITerminoEstandarDao;
import com.sigebi.entity.TerminoEstandar;
import com.sigebi.service.TerminoEstandarService;


@Service
public class TerminoEstandarServiceImpl implements TerminoEstandarService{

	@Autowired
	private ITerminoEstandarDao terminoEstandarDao;
	
	public TerminoEstandarServiceImpl(ITerminoEstandarDao terminoEstandarDao) {
        this.terminoEstandarDao = terminoEstandarDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<TerminoEstandar> findAll() {
		return (List<TerminoEstandar>) terminoEstandarDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) terminoEstandarDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public TerminoEstandar findById(int id) {
		return terminoEstandarDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public TerminoEstandar save(TerminoEstandar cliente) {
		return terminoEstandarDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		terminoEstandarDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<TerminoEstandar> buscar(Date fromDate, Date toDate, TerminoEstandar terminoEstandar, String orderBy, String orderDir, Pageable pageable) {
		List<TerminoEstandar> terminoEstandarList;
		
		Specification<TerminoEstandar> terminoEstandarSpec = (Specification<TerminoEstandar>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();

            if ( terminoEstandar.getId() != null ) {
                p = cb.and(p, cb.equal(root.get("id"), terminoEstandar.getId()) );
            }
            if (!StringUtils.isEmpty(terminoEstandar.getCodigoUnico())) {
                p = cb.and(p, cb.like(root.get("codigoUnico"), "%" + terminoEstandar.getCodigoUnico() + "%"));
            }
            if (!StringUtils.isEmpty(terminoEstandar.getTermino())) {
                p = cb.and(p, cb.like(root.get("termino"), "%" + terminoEstandar.getTermino() + "%"));
            }
           
            String orden = "id";
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
			terminoEstandarList = terminoEstandarDao.findAll(terminoEstandarSpec, pageable).getContent();			
		}else {
			terminoEstandarList = terminoEstandarDao.findAll(terminoEstandarSpec);
		}
        
        return terminoEstandarList;
    }

}
