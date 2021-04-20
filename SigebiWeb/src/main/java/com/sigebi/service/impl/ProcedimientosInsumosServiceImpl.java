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

import com.sigebi.dao.IProcedimientosInsumosDao;
import com.sigebi.entity.Pacientes;
import com.sigebi.entity.ProcedimientosInsumos;
import com.sigebi.service.ProcedimientosInsumosService;


@Service
public class ProcedimientosInsumosServiceImpl implements ProcedimientosInsumosService{

	@Autowired
	private IProcedimientosInsumosDao ProcedimientosInsumosDao;
	
	public ProcedimientosInsumosServiceImpl(IProcedimientosInsumosDao ProcedimientosInsumosDao) {
        this.ProcedimientosInsumosDao = ProcedimientosInsumosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<ProcedimientosInsumos> findAll() {
		return (List<ProcedimientosInsumos>) ProcedimientosInsumosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) ProcedimientosInsumosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public ProcedimientosInsumos findById(int id) {
		return ProcedimientosInsumosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public ProcedimientosInsumos save(ProcedimientosInsumos cliente) {
		return ProcedimientosInsumosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		ProcedimientosInsumosDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ProcedimientosInsumos> buscar(Date fromDate, Date toDate, ProcedimientosInsumos procedimientoInsumo, String orderBy, String orderDir, Pageable pageable) {
		List<ProcedimientosInsumos> ProcedimientosInsumosList;
		
		Specification<ProcedimientosInsumos> ProcedimientosInsumosSpec = (Specification<ProcedimientosInsumos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( procedimientoInsumo.getProcedimientoInsumoId() != null ) {
                p = cb.and(p, cb.equal(root.get("procedimientoInsumoId"), procedimientoInsumo.getProcedimientoInsumoId()) );
            }
            if ( procedimientoInsumo.getProcedimientos() != null && procedimientoInsumo.getProcedimientos().getProcedimientoId() != null) {
                p = cb.and(p, cb.equal(root.get("procedimientos"), procedimientoInsumo.getProcedimientos().getProcedimientoId()) );
            }
                        
            String orden = "procedimientoInsumoId";
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
			ProcedimientosInsumosList = ProcedimientosInsumosDao.findAll(ProcedimientosInsumosSpec, pageable).getContent();			
		}else {
			ProcedimientosInsumosList = ProcedimientosInsumosDao.findAll(ProcedimientosInsumosSpec);
		}
        
        return ProcedimientosInsumosList;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<ProcedimientosInsumos> buscarNoPaginable(Date fromDate, Date toDate, ProcedimientosInsumos procedimientoInsumo) {
		List<ProcedimientosInsumos> ProcedimientosInsumosList = ProcedimientosInsumosDao.findAll((Specification<ProcedimientosInsumos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( procedimientoInsumo.getProcedimientoInsumoId() != null ) {
                p = cb.and(p, cb.equal(root.get("procedimientoInsumoId"), procedimientoInsumo.getProcedimientoInsumoId()) );
            }
            if ( procedimientoInsumo.getProcedimientos() != null && procedimientoInsumo.getProcedimientos().getProcedimientoId() != null) {
                p = cb.and(p, cb.equal(root.get("procedimientos"), procedimientoInsumo.getProcedimientos().getProcedimientoId()) );
            }
                       
            cq.orderBy(cb.desc(root.get("procedimientoInsumoId")));            
            return p;
		});
        
        return ProcedimientosInsumosList;
    }
}
