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

import com.sigebi.dao.IPatologiasProcedimientosDao;
import com.sigebi.entity.PatologiasProcedimientos;
import com.sigebi.service.PatologiasProcedimientosService;


@Service
public class PatologiasProcedimientosServiceImpl implements PatologiasProcedimientosService{

	@Autowired
	private IPatologiasProcedimientosDao patologiasProcedimientosDao;
	
	public PatologiasProcedimientosServiceImpl(IPatologiasProcedimientosDao patologiasProcedimientosDao) {
        this.patologiasProcedimientosDao = patologiasProcedimientosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<PatologiasProcedimientos> findAll() {
		return (List<PatologiasProcedimientos>) patologiasProcedimientosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) patologiasProcedimientosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public PatologiasProcedimientos findById(int id) {
		return patologiasProcedimientosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public PatologiasProcedimientos save(PatologiasProcedimientos cliente) {
		return patologiasProcedimientosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		patologiasProcedimientosDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<PatologiasProcedimientos> buscar(Date fromDate, Date toDate, PatologiasProcedimientos patologiaProcedimiento, String orderBy, String orderDir, Pageable pageable) {
		List<PatologiasProcedimientos> patologiasProcedimientosList;
		
		Specification<PatologiasProcedimientos> patologiasProcedimientosSpec = (Specification<PatologiasProcedimientos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( patologiaProcedimiento.getPatologiaProcedimientoId() != null ) {
                p = cb.and(p, cb.equal(root.get("patologiaProcedimientoId"), patologiaProcedimiento.getPatologiaProcedimientoId()) );
            }
            if (!StringUtils.isEmpty(patologiaProcedimiento.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + patologiaProcedimiento.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(patologiaProcedimiento.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + patologiaProcedimiento.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(patologiaProcedimiento.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + patologiaProcedimiento.getEstado() + "%"));
            }
                        
            String orden = "patologiaProcedimientoId";
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
			patologiasProcedimientosList = patologiasProcedimientosDao.findAll(patologiasProcedimientosSpec, pageable).getContent();			
		}else {
			patologiasProcedimientosList = patologiasProcedimientosDao.findAll(patologiasProcedimientosSpec);
		}
        
        return patologiasProcedimientosList;
    }

}
