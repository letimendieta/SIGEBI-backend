package com.sigebi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.sigebi.dao.IAreasDao;
import com.sigebi.entity.Areas;
import com.sigebi.service.AreasService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class AreasServiceImpl implements AreasService{

	@Autowired
	private IAreasDao areasDao;
	
	@Autowired
	private UtilesService utiles;
	
	public AreasServiceImpl(IAreasDao areasDao) {
        this.areasDao = areasDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Areas> listar() throws SigebiException {
		
		List<Areas> areas = areasDao.findAll();
		
		if( areas.isEmpty()) {
			throw new SigebiException.DataNotFound("No se encontraron datos");
		}
		
		return areas;
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) areasDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Areas obtener(int id) throws SigebiException {
		
		Areas area = areasDao.findById(id).orElse(null);
				
		if ( area == null ) {
			String mensaje = "El área con ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		return area;
	}

	@Override
	@Transactional
	public Areas guardar(Areas area) {
		return areasDao.save(area);
	}
	
	@Override
	@Transactional
	public Areas actualizar(Areas area) throws SigebiException {
		
		if ( area.getAreaId() == null ) {
			throw new SigebiException.BusinessException("Área id es requerido ");
		}
		
		Areas areaActual = areasDao.findById(area.getAreaId()).orElse(null);
		
		if ( areaActual == null ) {
			String mensaje = "Error: no se pudo editar, el area ID: "
					.concat(String.valueOf(area.getAreaId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		return areasDao.save(area);
	}

	@Override
	@Transactional(readOnly = true)
	public void eliminar(int id) throws SigebiException {
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			throw new SigebiException.BusinessException("Área id es requerido");			
		}
		
		Areas areaActual = areasDao.findById(id).orElse(null);
		
		if ( areaActual == null ) {
			String mensaje = "El área ID: "
							.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		areasDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Areas> buscar(Date fromDate, Date toDate, Areas area, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException{
		List<Areas> areasList;
		
		Specification<Areas> areasSpec = (Specification<Areas>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( area.getAreaId() != null ) {
                p = cb.and(p, cb.equal(root.get("areaId"), area.getAreaId()) );
            }
            if (!StringUtils.isEmpty(area.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + area.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(area.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + area.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(area.getTipo())) {
                p = cb.and(p, cb.like(root.get("tipo"), "%" + area.getTipo() + "%"));
            }
            if (!StringUtils.isEmpty(area.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + area.getEstado() + "%"));
            }
                        
            String orden = "areaId";
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
			areasList = areasDao.findAll(areasSpec, pageable).getContent();			
		}else {
			areasList = areasDao.findAll(areasSpec);
		}
        
        return areasList;
    }

}
