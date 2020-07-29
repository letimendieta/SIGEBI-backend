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

import com.sigebi.dao.IHorariosDisponiblesDao;
import com.sigebi.entity.HorariosDisponibles;
import com.sigebi.service.HorariosDisponiblesService;


@Service
public class HorariosDisponiblesServiceImpl implements HorariosDisponiblesService{

	@Autowired
	private IHorariosDisponiblesDao horariosDisponiblesDao;
		
	public HorariosDisponiblesServiceImpl(IHorariosDisponiblesDao horariosDisponiblesDao) {
        this.horariosDisponiblesDao = horariosDisponiblesDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<HorariosDisponibles> findAll() {
		return (List<HorariosDisponibles>) horariosDisponiblesDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public HorariosDisponibles findById(int id) {
		return horariosDisponiblesDao.findById(id).orElse(null);
	}

	@Transactional
	public HorariosDisponibles guardar(HorariosDisponibles horariosDisponible) throws Exception {
						
		return horariosDisponiblesDao.save(horariosDisponible);
	}
	
	@Transactional
	public HorariosDisponibles actualizar(HorariosDisponibles horariosDisponible) throws Exception {
						
		return horariosDisponiblesDao.save(horariosDisponible);
	}

	@Override
	@Transactional
	public void delete(int id) {
		horariosDisponiblesDao.deleteById(id);
	}
	
	@Override
	public List<HorariosDisponibles> buscar(Date fromDate, Date toDate, 
										HorariosDisponibles horariosDisponible, 
										List<Integer> funcionariosId,
										Pageable pageable) {
		List<HorariosDisponibles> HorariosDisponiblesList = horariosDisponiblesDao.findAll((Specification<HorariosDisponibles>) (root, cq, cb) -> {
            
			Predicate p = cb.conjunction();
            if( funcionariosId != null && !funcionariosId.isEmpty() ){
            	p = cb.and(root.get("funcionarios").in(funcionariosId));
            }
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( horariosDisponible.getHorarioDisponibleId() != null ) {
                p = cb.and(p, cb.equal(root.get("horarioDisponibleId"), horariosDisponible.getHorarioDisponibleId()) );
            }
            if ( horariosDisponible.getFecha() != null ) {
                p = cb.and(p, cb.equal(root.get("fecha"), horariosDisponible.getFecha()) );
            }
            if ( horariosDisponible.getEstado() != null ) {
                p = cb.and(p, cb.equal(root.get("estado"), horariosDisponible.getEstado()) );
            }
            cq.orderBy(cb.desc(root.get("horarioDisponibleId")));
            return p;
        }, pageable).getContent();
        return HorariosDisponiblesList;
    }
	
}
