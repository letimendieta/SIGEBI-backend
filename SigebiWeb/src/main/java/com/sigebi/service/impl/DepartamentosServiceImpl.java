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

import com.sigebi.dao.IDepartamentosDao;
import com.sigebi.entity.Departamentos;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.service.DepartamentosService;


@Service
public class DepartamentosServiceImpl implements DepartamentosService{

	@Autowired
	private IDepartamentosDao departamentosDao;
	
	public DepartamentosServiceImpl(IDepartamentosDao departamentosDao) {
        this.departamentosDao = departamentosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Departamentos> findAll() {
		return (List<Departamentos>) departamentosDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Departamentos findById(int id) {
		return departamentosDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) departamentosDao.count();
	}

	@Override
	@Transactional
	public Departamentos save(Departamentos cliente) {
		return departamentosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		departamentosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Departamentos> buscar(Date fromDate, Date toDate, Departamentos departamento, String orderBy, String orderDir, Pageable pageable) {
		
		List<Departamentos> departamentosList;
		
		Specification<Departamentos> departamentosSpec = (Specification<Departamentos>) (root, cq, cb) -> {
		
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( departamento.getDepartamentoId() != null ) {
                p = cb.and(p, cb.equal(root.get("departamentoId"), departamento.getDepartamentoId()) );
            }
            if (!StringUtils.isEmpty(departamento.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + departamento.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(departamento.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + departamento.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(departamento.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + departamento.getEstado() + "%"));
            }
            String orden = "departamentoId";
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
        	departamentosList = departamentosDao.findAll(departamentosSpec, pageable).getContent();			
		}else {
			departamentosList = departamentosDao.findAll(departamentosSpec);
		}
        return departamentosList;
    }

}
