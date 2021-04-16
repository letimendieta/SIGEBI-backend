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

import com.sigebi.dao.IPreguntasDao;
import com.sigebi.entity.Preguntas;
import com.sigebi.service.PreguntasService;


@Service
public class PreguntasServiceImpl implements PreguntasService{

	@Autowired
	private IPreguntasDao preguntasDao;
	
	public PreguntasServiceImpl(IPreguntasDao preguntasDao) {
        this.preguntasDao = preguntasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Preguntas> findAll() {
		return (List<Preguntas>) preguntasDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) preguntasDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Preguntas findById(int id) {
		return preguntasDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Preguntas save(Preguntas cliente) {
		return preguntasDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		preguntasDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Preguntas> buscar(Date fromDate, Date toDate, Preguntas pregunta, String orderBy, String orderDir, Pageable pageable) {
		List<Preguntas> preguntasList;
		
		Specification<Preguntas> preguntasSpec = (Specification<Preguntas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( pregunta.getPreguntaId() != null ) {
                p = cb.and(p, cb.equal(root.get("preguntaId"), pregunta.getPreguntaId()) );
            }
            if (!StringUtils.isEmpty(pregunta.getNumero())) {
                p = cb.and(p, cb.equal(root.get("numero"), pregunta.getNumero()));
            }
            if (!StringUtils.isEmpty(pregunta.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + pregunta.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(pregunta.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + pregunta.getEstado() + "%"));
            }
            if ( pregunta.getAreas() != null && pregunta.getAreas().getAreaId() != null ) {
                p = cb.and(p, cb.equal(root.get("areas"), pregunta.getAreas().getAreaId()) );
            }
                        
            String orden = "preguntaId";
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
			preguntasList = preguntasDao.findAll(preguntasSpec, pageable).getContent();			
		}else {
			preguntasList = preguntasDao.findAll(preguntasSpec);
		}
        
        return preguntasList;
    }

}
