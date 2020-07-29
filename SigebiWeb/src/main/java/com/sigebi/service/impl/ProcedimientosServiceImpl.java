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

import com.sigebi.dao.IProcedimientosDao;
import com.sigebi.entity.Procedimientos;
import com.sigebi.service.ProcedimientosService;


@Service
public class ProcedimientosServiceImpl implements ProcedimientosService{

	@Autowired
	private IProcedimientosDao procedimientosDao;
		
	public ProcedimientosServiceImpl(IProcedimientosDao procedimientosDao) {
        this.procedimientosDao = procedimientosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedimientos> findAll() {
		return (List<Procedimientos>) procedimientosDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Procedimientos findById(int id) {
		return procedimientosDao.findById(id).orElse(null);
	}

	@Transactional
	public Procedimientos guardar(Procedimientos procedimiento) throws Exception {
						
		return procedimientosDao.save(procedimiento);
	}
	
	@Transactional
	public Procedimientos actualizar(Procedimientos procedimiento) throws Exception {
						
		return procedimientosDao.save(procedimiento);
	}

	@Override
	@Transactional
	public void delete(int id) {
		procedimientosDao.deleteById(id);
	}
	
	@Override
	public List<Procedimientos> buscar(Date fromDate, Date toDate, 
										Procedimientos procedimiento, 
										List<Integer> funcionariosId,
										List<Integer> pacientesId,
										Pageable pageable) {
		List<Procedimientos> ProcedimientosList = procedimientosDao.findAll((Specification<Procedimientos>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( funcionariosId != null && !funcionariosId.isEmpty() ){
            	p = cb.and(root.get("funcionarios").in(funcionariosId));
            }     
            if( pacientesId != null && !pacientesId.isEmpty() ){
            	p = cb.and(root.get("pacientes").in(pacientesId));
            } 
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( procedimiento.getProcedimientoId() != null ) {
                p = cb.and(p, cb.equal(root.get("procedimientoId"), procedimiento.getProcedimientoId()) );
            }
            if ( procedimiento.getFecha() != null ) {
                p = cb.and(p, cb.equal(root.get("fecha"), procedimiento.getFecha()) );
            }
            cq.orderBy(cb.desc(root.get("procedimientoId")));
            return p;
        }, pageable).getContent();
        return ProcedimientosList;
    }
	
}
