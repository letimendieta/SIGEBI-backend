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

import com.sigebi.dao.IParametrosDao;
import com.sigebi.entity.Parametros;
import com.sigebi.service.ParametrosService;


@Service
public class ParametrosServiceImpl implements ParametrosService{

	@Autowired
	private IParametrosDao parametrosDao;
		
	public ParametrosServiceImpl(IParametrosDao parametrosDao) {
        this.parametrosDao = parametrosDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Parametros> findAll() {
		return (List<Parametros>) parametrosDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Parametros findById(int id) {
		return parametrosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Parametros save(Parametros cliente) {
		return parametrosDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		parametrosDao.deleteById(id);
	}
	
	@Override
	@Transactional
	public List<Parametros> buscar(Date fromDate, Date toDate, Parametros parametro, String orderBy, String orderDir, Pageable pageable) {
						
        List<Parametros> parametrosList = parametrosDao.findAll((Specification<Parametros>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( parametro.getParametroId() != null ) {
                p = cb.and(p, cb.equal(root.get("parametroId"), parametro.getParametroId()) );
            }
            if (!StringUtils.isEmpty(parametro.getCodigoParametro())) {
                p = cb.and(p, cb.like(root.get("codigoParametro"), "%" + parametro.getCodigoParametro() + "%"));
            }
            if (!StringUtils.isEmpty(parametro.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + parametro.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(parametro.getValor())) {
                p = cb.and(p, cb.like(root.get("valor"), "%" + parametro.getValor() + "%"));
            }
            if (!StringUtils.isEmpty(parametro.getDescripcionValor())) {
                p = cb.and(p, cb.like(root.get("descripcionValor"), "%" + parametro.getDescripcionValor() + "%"));
            }
            String orden = "parametroId";
            if (!StringUtils.isEmpty(orderBy)) {
            	orden = orderBy;
            }
            if("asc".equalsIgnoreCase(orderDir)){
            	cq.orderBy(cb.asc(root.get(orden)));
            }else {
            	cq.orderBy(cb.desc(root.get(orden)));
            }            
            return p;
        }, pageable).getContent();
        return parametrosList;
    }

}
