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

import com.sigebi.dao.IDependenciasDao;
import com.sigebi.entity.Dependencias;
import com.sigebi.service.DependenciasService;


@Service
public class DependenciasServiceImpl implements DependenciasService{

	@Autowired
	private IDependenciasDao dependenciasDao;
	
	public DependenciasServiceImpl(IDependenciasDao dependenciasDao) {
        this.dependenciasDao = dependenciasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Dependencias> findAll() {
		return (List<Dependencias>) dependenciasDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Dependencias findById(int id) {
		return dependenciasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Dependencias save(Dependencias cliente) {
		return dependenciasDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		dependenciasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Dependencias> buscar(Date fromDate, Date toDate, Dependencias dependencia, String orderBy, String orderDir, Pageable pageable) {
		
        List<Dependencias> dependenciasList = dependenciasDao.findAll((Specification<Dependencias>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( dependencia.getDependenciaId() != null ) {
                p = cb.and(p, cb.equal(root.get("dependenciaId"), dependencia.getDependenciaId()) );
            }
            if (!StringUtils.isEmpty(dependencia.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + dependencia.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(dependencia.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + dependencia.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(dependencia.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + dependencia.getEstado() + "%"));
            }
            String orden = "dependenciaId";
            if (!StringUtils.isEmpty(orderBy)) {
            	orden = orderBy;
            }
            if("asc".equalsIgnoreCase(orderDir)){
            	cq.orderBy(cb.asc(root.get(orden)));
            }else {
            	cq.orderBy(cb.desc(root.get(orden)));
            }            
            return p;
        }, pageable).getContent();
        return dependenciasList;
    }

}
