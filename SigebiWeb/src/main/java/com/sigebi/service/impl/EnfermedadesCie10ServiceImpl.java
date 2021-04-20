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

import com.sigebi.dao.IEnfermedadesCie10Dao;
import com.sigebi.entity.EnfermedadesCie10;
import com.sigebi.service.EnfermedadesCie10Service;


@Service
public class EnfermedadesCie10ServiceImpl implements EnfermedadesCie10Service{

	@Autowired
	private IEnfermedadesCie10Dao enfermedadesCie10Dao;
	
	public EnfermedadesCie10ServiceImpl(IEnfermedadesCie10Dao enfermedadesCie10Dao) {
        this.enfermedadesCie10Dao = enfermedadesCie10Dao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<EnfermedadesCie10> findAll() {
		return (List<EnfermedadesCie10>) enfermedadesCie10Dao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) enfermedadesCie10Dao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public EnfermedadesCie10 findById(int id) {
		return enfermedadesCie10Dao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public EnfermedadesCie10 save(EnfermedadesCie10 cliente) {
		return enfermedadesCie10Dao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		enfermedadesCie10Dao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EnfermedadesCie10> buscar(Date fromDate, Date toDate, EnfermedadesCie10 enfermedadCie10, String orderBy, String orderDir, Pageable pageable) {
		List<EnfermedadesCie10> enfermedadesCie10List;
		
		Specification<EnfermedadesCie10> enfermedadesCie10Spec = (Specification<EnfermedadesCie10>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( enfermedadCie10.getEnfermedadCie10Id() != null ) {
                p = cb.and(p, cb.equal(root.get("enfermedadCie10Id"), enfermedadCie10.getEnfermedadCie10Id()) );
            }
            if (!StringUtils.isEmpty(enfermedadCie10.getCodigo())) {
                p = cb.and(p, cb.like(cb.lower(root.get("codigo")), "%" + enfermedadCie10.getCodigo().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(enfermedadCie10.getDescripcion())) {
                p = cb.and(p, cb.like(cb.lower(root.get("descripcion")), "%" + enfermedadCie10.getDescripcion().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(enfermedadCie10.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + enfermedadCie10.getEstado() + "%"));
            }
                        
            String orden = "enfermedadCie10Id";
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
			enfermedadesCie10List = enfermedadesCie10Dao.findAll(enfermedadesCie10Spec, pageable).getContent();			
		}else {
			enfermedadesCie10List = enfermedadesCie10Dao.findAll(enfermedadesCie10Spec);
		}
        
        return enfermedadesCie10List;
    }

}
