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

import com.sigebi.dao.IHistorialClinicoDao;
import com.sigebi.entity.HistorialClinico;
import com.sigebi.service.HistorialesClinicosService;


@Service
public class HistorialesClinicosServiceImpl implements HistorialesClinicosService{

	@Autowired
	private IHistorialClinicoDao historialClinicosDao;
	
	public HistorialesClinicosServiceImpl(IHistorialClinicoDao historialClinicosDao) {
        this.historialClinicosDao = historialClinicosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<HistorialClinico> findAll() {
		return (List<HistorialClinico>) historialClinicosDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public HistorialClinico findById(int id) {
		return historialClinicosDao.findById(id).orElse(null);
	}

	@Transactional
	public HistorialClinico guardar(HistorialClinico historialClinico) throws Exception {
						
		return historialClinicosDao.save(historialClinico);
	}
	
	@Transactional
	public HistorialClinico actualizar(HistorialClinico historialClinico) throws Exception {
						
		return historialClinicosDao.save(historialClinico);
	}

	@Override
	@Transactional
	public void delete(int id) {
		historialClinicosDao.deleteById(id);
	}
	
	@Override
	public List<HistorialClinico> buscar(Date fromDate, Date toDate, 
										HistorialClinico historialClinico, 
										List<Integer> pacientesId,
										Pageable pageable) {
		List<HistorialClinico> HistorialClinicoList = historialClinicosDao.findAll((Specification<HistorialClinico>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();            
            if( pacientesId != null && !pacientesId.isEmpty() ){
            	p = cb.and(root.get("pacientes").in(pacientesId));
            } 
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( historialClinico.getHistorialClinicoId() != null ) {
                p = cb.and(p, cb.equal(root.get("historialClinicoId"), historialClinico.getHistorialClinicoId()) );
            }
            if ( historialClinico.getAreaId() != null ) {
                p = cb.and(p, cb.equal(root.get("areaId"), historialClinico.getAreaId()) );
            }
           
            cq.orderBy(cb.desc(root.get("historialClinicoId")));
            return p;
        }, pageable).getContent();
        return HistorialClinicoList;
    }
	
}
