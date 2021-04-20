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

import com.sigebi.dao.ITratamientosInsumosDao;
import com.sigebi.entity.TratamientosInsumos;
import com.sigebi.service.TratamientosInsumosService;


@Service
public class TratamientosInsumosServiceImpl implements TratamientosInsumosService{

	@Autowired
	private ITratamientosInsumosDao tratamientosInsumosDao;
	
	public TratamientosInsumosServiceImpl(ITratamientosInsumosDao tratamientosInsumosDao) {
        this.tratamientosInsumosDao = tratamientosInsumosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<TratamientosInsumos> findAll() {
		return (List<TratamientosInsumos>) tratamientosInsumosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) tratamientosInsumosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public TratamientosInsumos findById(int id) {
		return tratamientosInsumosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public TratamientosInsumos save(TratamientosInsumos cliente) {
		return tratamientosInsumosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		tratamientosInsumosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<TratamientosInsumos> buscar(Date fromDate, Date toDate, TratamientosInsumos tratamientoInsumo, String orderBy, String orderDir, Pageable pageable) {
		List<TratamientosInsumos> tratamientosInsumosList;
		
		Specification<TratamientosInsumos> tratamientosInsumosSpec = (Specification<TratamientosInsumos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( tratamientoInsumo.getTratamientoInsumoId() != null ) {
                p = cb.and(p, cb.equal(root.get("tratamientoInsumoId"), tratamientoInsumo.getTratamientoInsumoId()) );
            }
            if ( tratamientoInsumo.getTratamientos() != null && tratamientoInsumo.getTratamientos().getTratamientoId() != null ) {
                p = cb.and(p, cb.equal(root.get("tratamientos"), tratamientoInsumo.getTratamientos().getTratamientoId()) );
            }
            if (!StringUtils.isEmpty(tratamientoInsumo.getCantidad())) {
            	p = cb.and(p, cb.equal(root.get("cantidad"), tratamientoInsumo.getCantidad()) );
            }
                        
            String orden = "tratamientoInsumoId";
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
			tratamientosInsumosList = tratamientosInsumosDao.findAll(tratamientosInsumosSpec, pageable).getContent();			
		}else {
			tratamientosInsumosList = tratamientosInsumosDao.findAll(tratamientosInsumosSpec);
		}
        
        return tratamientosInsumosList;
    }

}
