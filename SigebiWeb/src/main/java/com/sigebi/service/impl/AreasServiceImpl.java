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

import com.sigebi.dao.IAreasDao;
import com.sigebi.entity.Areas;
import com.sigebi.service.AreasService;


@Service
public class AreasServiceImpl implements AreasService{

	@Autowired
	private IAreasDao areasDao;
	
	public AreasServiceImpl(IAreasDao areasDao) {
        this.areasDao = areasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Areas> findAll() {
		return (List<Areas>) areasDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Areas findById(int id) {
		return areasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Areas save(Areas cliente) {
		return areasDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		areasDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Areas> buscar(Date fromDate, Date toDate, Areas area, Pageable pageable) {
		
        List<Areas> areasList = areasDao.findAll((Specification<Areas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( area.getAreaId() != null ) {
                p = cb.and(p, cb.equal(root.get("areaId"), area.getAreaId()) );
            }
            if (!StringUtils.isEmpty(area.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + area.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(area.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + area.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(area.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + area.getEstado() + "%"));
            }
            cq.orderBy(cb.desc(root.get("areaId")));
            return p;
        }, pageable).getContent();
        return areasList;
    }

}
