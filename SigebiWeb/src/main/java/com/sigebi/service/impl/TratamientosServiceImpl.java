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

import com.sigebi.dao.ITratamientosDao;
import com.sigebi.entity.Tratamientos;
import com.sigebi.service.TratamientosService;


@Service
public class TratamientosServiceImpl implements TratamientosService{

	@Autowired
	private ITratamientosDao tratamientosDao;
	
	public TratamientosServiceImpl(ITratamientosDao tratamientosDao) {
        this.tratamientosDao = tratamientosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Tratamientos> findAll() {
		return (List<Tratamientos>) tratamientosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) tratamientosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Tratamientos findById(int id) {
		return tratamientosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Tratamientos save(Tratamientos cliente) {
		return tratamientosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		tratamientosDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Tratamientos> buscar(Date fromDate, Date toDate, Tratamientos tratamiento, String orderBy, String orderDir, Pageable pageable) {
		List<Tratamientos> tratamientosList;
		
		Specification<Tratamientos> tratamientosSpec = (Specification<Tratamientos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( tratamiento.getTratamientoId() != null ) {
                p = cb.and(p, cb.equal(root.get("tratamientoId"), tratamiento.getTratamientoId()) );
            }
                        
            String orden = "tratamientoId";
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
			tratamientosList = tratamientosDao.findAll(tratamientosSpec, pageable).getContent();			
		}else {
			tratamientosList = tratamientosDao.findAll(tratamientosSpec);
		}
        
        return tratamientosList;
    }

}
