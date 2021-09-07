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

import com.sigebi.dao.IHorariosDisponiblesDao;
import com.sigebi.entity.Funcionarios;
import com.sigebi.entity.HorariosDisponibles;
import com.sigebi.service.FuncionariosService;
import com.sigebi.service.HorariosDisponiblesService;
import com.sigebi.service.PersonasService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class HorariosDisponiblesServiceImpl implements HorariosDisponiblesService{

	@Autowired
	private IHorariosDisponiblesDao horariosDisponiblesDao;
	@Autowired
	private FuncionariosService funcionariosService;
		
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
	public int count() {
		return (int) horariosDisponiblesDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public HorariosDisponibles findById(int id) {
		return horariosDisponiblesDao.findById(id).orElse(null);
	}

	@Transactional
	public HorariosDisponibles guardar(HorariosDisponibles horariosDisponible) throws Exception {
		
		if( horariosDisponible.getFuncionarios() == null || horariosDisponible.getFuncionarios().getFuncionarioId() == null ){
			throw new SigebiException.BusinessException("Funcionario es requerido ");
		}
		Funcionarios funcionario = funcionariosService.findById(horariosDisponible.getFuncionarios().getFuncionarioId());
		
		if( Globales.Estados.INACTIVO.equals(funcionario.getEstado()) ){
			throw new SigebiException.BusinessException("El funcionario se encuentra inactivo");
		}
		
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
	@Transactional(readOnly = true)
	public List<HorariosDisponibles> buscar(Date fromDate, Date toDate, 
										HorariosDisponibles horariosDisponible, 
										List<Integer> funcionariosId, String orderBy, String orderDir, Pageable pageable){
		List<HorariosDisponibles> HorariosDisponiblesList;
		
		Specification<HorariosDisponibles> horariosDisponibleSpec = (Specification<HorariosDisponibles>) (root, cq, cb) -> {
		            
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
            if ( horariosDisponible.getDia() != null ) {
                p = cb.and(p, cb.equal(root.get("dia"), horariosDisponible.getDia()) );
            }
            if ( horariosDisponible.getEstado() != null ) {
                p = cb.and(p, cb.equal(root.get("estado"), horariosDisponible.getEstado()) );
            }
            String orden = "horarioDisponibleId";
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
        	HorariosDisponiblesList = horariosDisponiblesDao.findAll(horariosDisponibleSpec, pageable).getContent();			
		}else {
			HorariosDisponiblesList = horariosDisponiblesDao.findAll(horariosDisponibleSpec);
		}
        return HorariosDisponiblesList;
    }
	
}
