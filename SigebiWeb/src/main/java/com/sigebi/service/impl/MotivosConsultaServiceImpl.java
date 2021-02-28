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

import com.sigebi.dao.IMotivosConsultaDao;
import com.sigebi.entity.MotivosConsulta;
import com.sigebi.service.MotivosConsultaService;


@Service
public class MotivosConsultaServiceImpl implements MotivosConsultaService{

	@Autowired
	private IMotivosConsultaDao motivosConsultaDao;
	
	public MotivosConsultaServiceImpl(IMotivosConsultaDao motivosConsultaDao) {
        this.motivosConsultaDao = motivosConsultaDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<MotivosConsulta> findAll() {
		return (List<MotivosConsulta>) motivosConsultaDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) motivosConsultaDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public MotivosConsulta findById(int id) {
		return motivosConsultaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public MotivosConsulta save(MotivosConsulta cliente) {
		return motivosConsultaDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		motivosConsultaDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<MotivosConsulta> buscar(Date fromDate, Date toDate, MotivosConsulta motivoConsulta, String orderBy, String orderDir, Pageable pageable) {
		List<MotivosConsulta> motivosConsultaList;
		
		Specification<MotivosConsulta> motivosConsultaSpec = (Specification<MotivosConsulta>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( motivoConsulta.getMotivoConsultaId() != null ) {
                p = cb.and(p, cb.equal(root.get("motivoConsultaId"), motivoConsulta.getMotivoConsultaId()) );
            }
            if (!StringUtils.isEmpty(motivoConsulta.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + motivoConsulta.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(motivoConsulta.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + motivoConsulta.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(motivoConsulta.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + motivoConsulta.getEstado() + "%"));
            }
                        
            String orden = "motivoConsultaId";
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
			motivosConsultaList = motivosConsultaDao.findAll(motivosConsultaSpec, pageable).getContent();			
		}else {
			motivosConsultaList = motivosConsultaDao.findAll(motivosConsultaSpec);
		}
        
        return motivosConsultaList;
    }

}
