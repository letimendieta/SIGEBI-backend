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

import com.sigebi.dao.ICarrerasDao;
import com.sigebi.entity.Carreras;
import com.sigebi.service.CarrerasService;


@Service
public class CarrerasServiceImpl implements CarrerasService{

	@Autowired
	private ICarrerasDao carrerasDao;
	
	public CarrerasServiceImpl(ICarrerasDao carrerasDao) {
        this.carrerasDao = carrerasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Carreras> findAll() {
		return (List<Carreras>) carrerasDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) carrerasDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Carreras findById(int id) {
		return carrerasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Carreras save(Carreras cliente) {
		return carrerasDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		carrerasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Carreras> buscar(Date fromDate, Date toDate, Carreras carrera, String orderBy, String orderDir, Pageable pageable) {
		
		List<Carreras> carrerasList;
		
		Specification<Carreras> carrerasSpec = (Specification<Carreras>) (root, cq, cb) -> {
		    Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( carrera.getCarreraId() != null ) {
                p = cb.and(p, cb.equal(root.get("carreraId"), carrera.getCarreraId()) );
            }
            if (!StringUtils.isEmpty(carrera.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + carrera.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(carrera.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + carrera.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(carrera.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + carrera.getEstado() + "%"));
            }
            String orden = "carreraId";
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
        	carrerasList = carrerasDao.findAll(carrerasSpec, pageable).getContent();			
		}else {
			carrerasList = carrerasDao.findAll(carrerasSpec);
		}
        return carrerasList;
    }

}
