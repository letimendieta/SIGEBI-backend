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

import com.sigebi.dao.IMovimientoInsumoDao;
import com.sigebi.entity.MovimientosInsumos;
import com.sigebi.service.AreasService;
import com.sigebi.service.MovimientosInsumosService;
import com.sigebi.service.UtilesService;
import com.sigebi.util.exceptions.SigebiException;


@Service
public class MovimientosInsumosServiceImpl implements MovimientosInsumosService{

	@Autowired
	private IMovimientoInsumoDao movimientoInsumoDao;
	
	@Autowired
	private UtilesService utiles;
	
	public MovimientosInsumosServiceImpl(IMovimientoInsumoDao movimientoInsumoDao) {
        this.movimientoInsumoDao = movimientoInsumoDao;
    }
	
	/*@Override
	@Transactional(readOnly = true)
	public List<MovimientosInsumos> listar() throws SigebiException {
		
		List<MovimientosInsumos> MovimientosInsumos = movimientoInsumoDao.findAll();
		
		if( MovimientosInsumos.isEmpty()) {
			throw new SigebiException.DataNotFound("No se encontraron datos");
		}
		
		return MovimientosInsumos;
	}
	
	@Override
	@Transactional(readOnly = true)
	public int count() {
		return (int) movimientoInsumoDao.count();
	}

	@Override
	@Transactional(readOnly = true)
	public MovimientosInsumos obtener(int id) throws SigebiException {
		
		MovimientosInsumos movimientoInsumo = movimientoInsumoDao.findById(id).orElse(null);
				
		if ( movimientoInsumo == null ) {
			String mensaje = "El área con ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		return movimientoInsumo;
	}*/

	@Override
	@Transactional
	public MovimientosInsumos guardar(MovimientosInsumos movimientoInsumo) {
		return movimientoInsumoDao.save(movimientoInsumo);
	}
	
	/*@Override
	@Transactional
	public MovimientosInsumos actualizar(MovimientosInsumos movimientoInsumo) throws SigebiException {
		
		if ( movimientoInsumo.getAreaId() == null ) {
			throw new SigebiException.BusinessException("Área id es requerido ");
		}
		
		MovimientosInsumos areaActual = movimientoInsumoDao.findById(movimientoInsumo.getAreaId()).orElse(null);
		
		if ( areaActual == null ) {
			String mensaje = "Error: no se pudo editar, el movimientoInsumo ID: "
					.concat(String.valueOf(movimientoInsumo.getAreaId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		return movimientoInsumoDao.save(movimientoInsumo);
	}

	@Override
	@Transactional
	public void eliminar(int id) throws SigebiException {
		
		if ( utiles.isNullOrBlank(String.valueOf(id)) ) {
			throw new SigebiException.BusinessException("Área id es requerido");			
		}
		
		MovimientosInsumos areaActual = movimientoInsumoDao.findById(id).orElse(null);
		
		if ( areaActual == null ) {
			String mensaje = "El área ID: "
							.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		movimientoInsumoDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<MovimientosInsumos> buscar(Date fromDate, Date toDate, MovimientosInsumos movimientoInsumo, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException{
		List<MovimientosInsumos> areasList;
		
		Specification<MovimientosInsumos> areasSpec = (Specification<MovimientosInsumos>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( movimientoInsumo.getAreaId() != null ) {
                p = cb.and(p, cb.equal(root.get("areaId"), movimientoInsumo.getAreaId()) );
            }
            if (!StringUtils.isEmpty(movimientoInsumo.getCodigo())) {
                p = cb.and(p, cb.like(root.get("codigo"), "%" + movimientoInsumo.getCodigo() + "%"));
            }
            if (!StringUtils.isEmpty(movimientoInsumo.getDescripcion())) {
                p = cb.and(p, cb.like(root.get("descripcion"), "%" + movimientoInsumo.getDescripcion() + "%"));
            }
            if (!StringUtils.isEmpty(movimientoInsumo.getEstado())) {
                p = cb.and(p, cb.like(root.get("estado"), "%" + movimientoInsumo.getEstado() + "%"));
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
			areasList = movimientoInsumoDao.findAll(areasSpec, pageable).getContent();			
		}else {
			areasList = movimientoInsumoDao.findAll(areasSpec);
		}
        
        return areasList;
    }*/

}
