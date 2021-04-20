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

import com.sigebi.dao.IPreguntasHistorialDao;
import com.sigebi.entity.PreguntasHistorial;
import com.sigebi.service.PreguntasHistorialService;


@Service
public class PreguntasHistorialServiceImpl implements PreguntasHistorialService{

	@Autowired
	private IPreguntasHistorialDao preguntasHistorialDao;
	
	public PreguntasHistorialServiceImpl(IPreguntasHistorialDao preguntasDao) {
        this.preguntasHistorialDao = preguntasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<PreguntasHistorial> findAll() {
		return (List<PreguntasHistorial>) preguntasHistorialDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) preguntasHistorialDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public PreguntasHistorial findById(int id) {
		return preguntasHistorialDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public PreguntasHistorial save(PreguntasHistorial cliente) {
		return preguntasHistorialDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		preguntasHistorialDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<PreguntasHistorial> buscar(Date fromDate, Date toDate, PreguntasHistorial preguntaHistorial, String orderBy, String orderDir, Pageable pageable) {
		List<PreguntasHistorial> preguntasList;
		
		Specification<PreguntasHistorial> preguntasSpec = (Specification<PreguntasHistorial>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( preguntaHistorial.getPreguntaHistorialId() != null ) {
                p = cb.and(p, cb.equal(root.get("preguntaHistorialId"), preguntaHistorial.getPreguntaHistorialId()) );
            }
            if (!StringUtils.isEmpty(preguntaHistorial.getHistorialClinicoId())) {
                p = cb.and(p, cb.equal(root.get("historialClinicoId"), preguntaHistorial.getHistorialClinicoId()));
            }
            if ( preguntaHistorial.getPreguntas() != null && preguntaHistorial.getPreguntas().getPreguntaId() != null ) {
                p = cb.and(p, cb.equal(root.get("preguntas"), preguntaHistorial.getPreguntas().getPreguntaId()) );
            }
                        
            String orden = "preguntaHistorialId";
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
			preguntasList = preguntasHistorialDao.findAll(preguntasSpec, pageable).getContent();			
		}else {
			preguntasList = preguntasHistorialDao.findAll(preguntasSpec);
		}
        
        return preguntasList;
    }

}
