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

import com.sigebi.dao.IInsumosDao;
import com.sigebi.entity.Insumos;
import com.sigebi.service.InsumosService;


@Service
public class InsumosServiceImpl implements InsumosService{

	@Autowired
	private IInsumosDao insumosDao;
	
	public InsumosServiceImpl(IInsumosDao insumosDao) {
        this.insumosDao = insumosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Insumos> findAll() {
		return (List<Insumos>) insumosDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Insumos findById(int id) {
		return insumosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Insumos save(Insumos cliente) {
		return insumosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		insumosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Insumos> buscar(Date fromDate, Date toDate, Insumos insumo, Pageable pageable) {
		
        List<Insumos> insumosList = insumosDao.findAll((Specification<Insumos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( insumo.getInsumoId() != null ) {
                p = cb.and(p, cb.equal(root.get("insumoId"), insumo.getInsumoId()) );
            }
            if (!StringUtils.isEmpty(insumo.getCodigo())) {
                p = cb.and(p, cb.like(cb.lower(root.get("codigo")), "%" + insumo.getCodigo().toLowerCase() + "%"));
            }
            if (!StringUtils.isEmpty(insumo.getTipo())) {
                p = cb.and(p, cb.like(root.get("tipo"), "%" + insumo.getTipo() + "%"));
            }
            if (!StringUtils.isEmpty(insumo.getDescripcion())) {
                p = cb.and(p, cb.like(cb.lower(root.get("descripcion")), "%" + insumo.getDescripcion().toLowerCase() + "%"));
            }
            if ( insumo.getFechaVencimiento() != null ) {
                p = cb.and(p, cb.equal(root.get("fechaVencimiento"), insumo.getFechaVencimiento()) );
            }
            cq.orderBy(cb.desc(root.get("insumoId")));
            return p;
        }, pageable).getContent();
        return insumosList;
    }

}
