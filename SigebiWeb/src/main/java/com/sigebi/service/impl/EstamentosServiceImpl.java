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

import com.sigebi.dao.IEstamentosDao;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.entity.Estamentos;
import com.sigebi.service.EstamentosService;


@Service
public class EstamentosServiceImpl implements EstamentosService{

	@Autowired
	private IEstamentosDao estamentosDao;
	
	public EstamentosServiceImpl(IEstamentosDao estamentosDao) {
        this.estamentosDao = estamentosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Estamentos> findAll() {
		return (List<Estamentos>) estamentosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) estamentosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Estamentos findById(int id) {
		return estamentosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Estamentos save(Estamentos cliente) {
		return estamentosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		estamentosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Estamentos> buscar(Date fromDate, Date toDate, Estamentos estamento, String orderBy, String orderDir, Pageable pageable) {
		List<Estamentos> estamentosList;
		
		Specification<Estamentos> estamentoSpec = (Specification<Estamentos>) (root, cq, cb) -> {
		
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( estamento.getEstamentoId() != null ) {
                p = cb.and(p, cb.equal(root.get("estamentoId"), estamento.getEstamentoId()) );
            }
            if (!StringUtils.isEmpty(estamento.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + estamento.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(estamento.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + estamento.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(estamento.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + estamento.getEstado() + "%"));
            }
            String orden = "estamentoId";
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
        	estamentosList = estamentosDao.findAll(estamentoSpec, pageable).getContent();			
		}else {
			estamentosList = estamentosDao.findAll(estamentoSpec);
		}
        return estamentosList;
    }

}
