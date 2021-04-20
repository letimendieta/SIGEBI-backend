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

import com.sigebi.dao.IVacunasDao;
import com.sigebi.entity.Vacunas;
import com.sigebi.service.VacunasService;


@Service
public class VacunasServiceImpl implements VacunasService{

	@Autowired
	private IVacunasDao vacunasDao;
	
	public VacunasServiceImpl(IVacunasDao vacunasDao) {
        this.vacunasDao = vacunasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Vacunas> findAll() {
		return (List<Vacunas>) vacunasDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) vacunasDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Vacunas findById(int id) {
		return vacunasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Vacunas save(Vacunas cliente) {
		return vacunasDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		vacunasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Vacunas> buscar(Date fromDate, Date toDate, Vacunas vacuna, String orderBy, String orderDir, Pageable pageable) {
		List<Vacunas> vacunasList;
		
		Specification<Vacunas> vacunasSpec = (Specification<Vacunas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( vacuna.getVacunaId() != null ) {
                p = cb.and(p, cb.equal(root.get("vacunaId"), vacuna.getVacunaId()) );
            }
            if (!StringUtils.isEmpty(vacuna.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + vacuna.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(vacuna.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + vacuna.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(vacuna.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + vacuna.getEstado() + "%"));
            }
                        
            String orden = "vacunaId";
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
			vacunasList = vacunasDao.findAll(vacunasSpec, pageable).getContent();			
		}else {
			vacunasList = vacunasDao.findAll(vacunasSpec);
		}
        
        return vacunasList;
    }

}
