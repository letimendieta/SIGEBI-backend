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

import com.sigebi.clases.ProcesoProcedimientos;
import com.sigebi.dao.IProcedimientosDao;
import com.sigebi.dao.IProcedimientosInsumosDao;
import com.sigebi.dao.IStockDao;
import com.sigebi.entity.Insumos;
import com.sigebi.entity.Procedimientos;
import com.sigebi.entity.ProcedimientosInsumos;
import com.sigebi.entity.Stock;
import com.sigebi.service.ProcedimientosInsumosService;
import com.sigebi.service.ProcedimientosService;


@Service
public class ProcedimientosServiceImpl implements ProcedimientosService{

	@Autowired
	private IProcedimientosDao procedimientosDao;
	@Autowired
	private IProcedimientosInsumosDao procedimientosInsumosDao;
	@Autowired
	private ProcedimientosInsumosService procedimientosInsumosService;
	@Autowired
	private IStockDao stockDao;
		
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
	public Procedimientos guardar(ProcesoProcedimientos procesoProcedimiento) throws Exception {
		procesoProcedimiento.getProcedimiento().setCantidadInsumo(procesoProcedimiento.getProcedimientoInsumoList().size());
		Procedimientos procedimientoResult = procedimientosDao.save(procesoProcedimiento.getProcedimiento());
		
		//Guardar el procedimiento insumo
		try {
			for(ProcedimientosInsumos procedimientoInsumo : procesoProcedimiento.getProcedimientoInsumoList()) {
				procedimientoInsumo.setProcedimientos(procedimientoResult);
				
				procedimientosInsumosDao.save(procedimientoInsumo);
				//Realizar el descuento de insumos
				int cantidadUsada = 0;
				if ( procedimientoInsumo.getCantidad() != null ) {
					cantidadUsada = procedimientoInsumo.getCantidad();
				}						
				
				if( cantidadUsada > 0) {
					descontarStock(procedimientoInsumo);
				}
			}			
		} catch (Exception e) {
			throw new Exception("Error al guardar los insumos utilizados " + e.getMessage());
		}
				
		return procedimientoResult;
	}
	
	@Transactional
	public Procedimientos actualizar(ProcesoProcedimientos procesoProcedimiento) throws Exception {
				
		ProcedimientosInsumos procedimientoBusqueda = new ProcedimientosInsumos();
		procedimientoBusqueda.setProcedimientos(new Procedimientos());
		
		procedimientoBusqueda.getProcedimientos().setProcedimientoId(procesoProcedimiento.getProcedimiento().getProcedimientoId());
		
		List<ProcedimientosInsumos> procedimientoInsumoDbList = procedimientosInsumosService.buscarNoPaginable(null, null, procedimientoBusqueda);
		
		try {
			//Actualizar el estado de los insumos si se cambio
			for(ProcedimientosInsumos procedimientoInsumo : procesoProcedimiento.getProcedimientoInsumoList()) {
				for(ProcedimientosInsumos procedimientoInsumoDb : procedimientoInsumoDbList) {										
					if( procedimientoInsumoDb.getProcedimientoInsumoId().equals(procedimientoInsumo.getProcedimientoInsumoId()) ) {
						if( !procedimientoInsumoDb.getEstado().equals(procedimientoInsumo.getEstado())) {							
							procedimientosInsumosDao.save(procedimientoInsumo);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("Error al actualizar los insumos utilizados " + e.getMessage());
		}
		
		return procedimientosDao.save(procesoProcedimiento.getProcedimiento());
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
	
	public void descontarStock(ProcedimientosInsumos procedimientoInsumo) throws Exception {
		try {
			Insumos insumo = new Insumos();
			insumo.setInsumoId(procedimientoInsumo.getInsumos().getInsumoId());
			Stock stockAdescontar = stockDao.findByInsumos(insumo);
			
			int cantidadActual = stockAdescontar.getCantidad();
			int cantidadUsada = procedimientoInsumo.getCantidad();
			
			if( cantidadActual <= 0) {
				throw new Exception("El insumo "+ stockAdescontar.getInsumos().getInsumoId() 
						+ " - " + stockAdescontar.getInsumos().getDescripcion()
						+" no cuenta con stock");
			}
			
			if( cantidadActual < cantidadUsada) {
				throw new Exception("El insumo "+ stockAdescontar.getInsumos().getInsumoId() 
						+ " - " + stockAdescontar.getInsumos().getDescripcion()
						+" no cuenta con stock suficiente, cantidad stock: " + cantidadActual +", "
						+" cantidad usada: " + cantidadUsada);
			}
			
			stockAdescontar.setCantidad(cantidadActual - cantidadUsada);
			
			stockDao.save(stockAdescontar);
			
		} catch (Exception e) {
			throw new Exception("Error al descontar stock " + e.getMessage());
		}
	}
	
}
