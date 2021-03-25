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

import com.sigebi.dao.IVacunacionesDao;
import com.sigebi.entity.Vacunaciones;
import com.sigebi.service.VacunacionesService;


@Service
public class VacunacionesServiceImpl implements VacunacionesService{

	@Autowired
	private IVacunacionesDao vacunacionesDao;
	
	public VacunacionesServiceImpl(IVacunacionesDao vacunacionesDao) {
        this.vacunacionesDao = vacunacionesDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Vacunaciones> findAll() {
		return (List<Vacunaciones>) vacunacionesDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) vacunacionesDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Vacunaciones findById(int id) {
		return vacunacionesDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Vacunaciones save(Vacunaciones cliente) {
		return vacunacionesDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		vacunacionesDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Vacunaciones> buscar(Date fromDate, Date toDate, Vacunaciones vacunacion, String orderBy, String orderDir, Pageable pageable) {
		List<Vacunaciones> vacunacioncionesList;
		
		Specification<Vacunaciones> vacunacioncionesSpec = (Specification<Vacunaciones>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( vacunacion.getVacunacionId() != null ) {
                p = cb.and(p, cb.equal(root.get("vacunacionId"), vacunacion.getVacunacionId()) );
            }
            if (!StringUtils.isEmpty(vacunacion.getVacunas())) {
                p = cb.and(p, cb.like(root.get("vacunaId"), "%" + vacunacion.getVacunas() + "%"));
            }
            if (!StringUtils.isEmpty(vacunacion.getPacienteId())) {
                p = cb.and(p, cb.like(root.get("pacienteId"), "%" + vacunacion.getPacienteId() + "%"));
            }            
                        
            String orden = "vacunacionId";
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
			vacunacioncionesList = vacunacionesDao.findAll(vacunacioncionesSpec, pageable).getContent();			
		}else {
			vacunacioncionesList = vacunacionesDao.findAll(vacunacioncionesSpec);
		}
        
        return vacunacioncionesList;
    }

}
