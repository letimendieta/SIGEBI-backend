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

import com.sigebi.dao.IConsultasDao;
import com.sigebi.entity.Consultas;
import com.sigebi.service.ConsultasService;


@Service
public class ConsultasServiceImpl implements ConsultasService{

	@Autowired
	private IConsultasDao consultasDao;
	
	public ConsultasServiceImpl(IConsultasDao consultasDao) {
        this.consultasDao = consultasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Consultas> findAll() {
		return (List<Consultas>) consultasDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) consultasDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Consultas findById(int id) {
		return consultasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Consultas save(Consultas cliente) {
		return consultasDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		consultasDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Consultas> buscar(Date fromDate, Date toDate, Consultas consulta, String orderBy, String orderDir, Pageable pageable) {
		List<Consultas> consultasList;
		
		Specification<Consultas> consultasSpec = (Specification<Consultas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if ( consulta.getFecha() != null ) {
                p = cb.and(p, cb.equal( root.get("fecha"), consulta.getFecha()) );
            }
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( consulta.getConsultaId() != null ) {
                p = cb.and(p, cb.equal(root.get("consultaId"), consulta.getConsultaId()) );
            }
                        
            String orden = "consultaId";
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
			consultasList = consultasDao.findAll(consultasSpec, pageable).getContent();			
		}else {
			consultasList = consultasDao.findAll(consultasSpec);
		}
        
        return consultasList;
    }

}
