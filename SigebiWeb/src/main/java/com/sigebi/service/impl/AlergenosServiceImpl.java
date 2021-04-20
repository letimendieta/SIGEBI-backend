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

import com.sigebi.dao.IAlergenosDao;
import com.sigebi.entity.Alergenos;
import com.sigebi.service.AlergenosService;


@Service
public class AlergenosServiceImpl implements AlergenosService{

	@Autowired
	private IAlergenosDao alergenosDao;
	
	public AlergenosServiceImpl(IAlergenosDao alergenosDao) {
        this.alergenosDao = alergenosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Alergenos> findAll() {
		return (List<Alergenos>) alergenosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) alergenosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Alergenos findById(int id) {
		return alergenosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Alergenos save(Alergenos cliente) {
		return alergenosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		alergenosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Alergenos> buscar(Date fromDate, Date toDate, Alergenos alergeno, String orderBy, String orderDir, Pageable pageable) {
		List<Alergenos> alergenosList;
		
		Specification<Alergenos> alergenosSpec = (Specification<Alergenos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( alergeno.getAlergenoId() != null ) {
                p = cb.and(p, cb.equal(root.get("alergenoId"), alergeno.getAlergenoId()) );
            }
            if (!StringUtils.isEmpty(alergeno.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + alergeno.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(alergeno.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + alergeno.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(alergeno.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + alergeno.getEstado() + "%"));
            }
                        
            String orden = "alergenoId";
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
			alergenosList = alergenosDao.findAll(alergenosSpec, pageable).getContent();			
		}else {
			alergenosList = alergenosDao.findAll(alergenosSpec);
		}
        
        return alergenosList;
    }

}
