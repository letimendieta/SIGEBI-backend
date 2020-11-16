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

import com.sigebi.dao.IDiagnosticosDao;
import com.sigebi.entity.Diagnosticos;
import com.sigebi.service.DiagnosticosService;


@Service
public class DiagnosticosServiceImpl implements DiagnosticosService{

	@Autowired
	private IDiagnosticosDao diagnosticosDao;
	
	public DiagnosticosServiceImpl(IDiagnosticosDao diagnosticosDao) {
        this.diagnosticosDao = diagnosticosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Diagnosticos> findAll() {
		return (List<Diagnosticos>) diagnosticosDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) diagnosticosDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Diagnosticos findById(int id) {
		return diagnosticosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Diagnosticos save(Diagnosticos cliente) {
		return diagnosticosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		diagnosticosDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Diagnosticos> buscar(Date fromDate, Date toDate, Diagnosticos diagnostico, String orderBy, String orderDir, Pageable pageable) {
		List<Diagnosticos> diagnosticosList;
		
		Specification<Diagnosticos> diagnosticosSpec = (Specification<Diagnosticos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( diagnostico.getDiagnosticoId() != null ) {
                p = cb.and(p, cb.equal(root.get("diagnosticoId"), diagnostico.getDiagnosticoId()) );
            }
                               
            String orden = "diagnosticoId";
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
			diagnosticosList = diagnosticosDao.findAll(diagnosticosSpec, pageable).getContent();			
		}else {
			diagnosticosList = diagnosticosDao.findAll(diagnosticosSpec);
		}
        
        return diagnosticosList;
    }

}
