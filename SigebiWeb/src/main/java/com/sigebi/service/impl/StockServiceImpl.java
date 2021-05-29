package com.sigebi.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IMovimientoInsumoDao;
import com.sigebi.dao.IStockDao;
import com.sigebi.entity.Areas;
import com.sigebi.entity.InsumosMedicos;
import com.sigebi.entity.Medicamentos;
import com.sigebi.entity.MovimientosInsumos;
import com.sigebi.entity.Stock;
import com.sigebi.service.StockService;
import com.sigebi.util.Globales;
import com.sigebi.util.exceptions.SigebiException;
import com.sigebi.util.exceptions.SigebiException.DataNotFound;


@Service
public class StockServiceImpl implements StockService{

	@Autowired
	private IStockDao stockDao;
	@Autowired
	private IMovimientoInsumoDao movimientoInsumoDao;
	
	public StockServiceImpl(IStockDao stockDao) {
        this.stockDao = stockDao;
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<Stock> findAll() {
		return (List<Stock>) stockDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Stock obtener(int id) throws SigebiException {
		
		Stock stock = stockDao.findById(id).orElse(null);
		
		if( stock == null ) {			
			String mensaje = "El stock con ID: "
					.concat(String.valueOf(id).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		if( stock.getInsumosMedicos() == null) {
			stock.setInsumosMedicos(new InsumosMedicos());
		}
		if( stock.getMedicamentos() == null) {
			stock.setMedicamentos(new Medicamentos());
		}
		
		return stock;
	}

	@Override
	@Transactional
	public Stock save(Stock stock) throws SigebiException {
		InsumosMedicos insumoMedico = new InsumosMedicos();
		Medicamentos medicamento = new Medicamentos();
		Stock stockExistente = null;
		
		//validaciones
		if( stock.getInsumosMedicos().getInsumoMedicoId() == null && stock.getMedicamentos().getMedicamentoId() == null ) {
			throw new SigebiException.BusinessException("Debe ingresar un insumo o un medicamento");
		}
		if( stock.getInsumosMedicos().getInsumoMedicoId() != null && stock.getMedicamentos().getMedicamentoId() != null ) {
			throw new SigebiException.BusinessException("No puede crearse un stock con un insumo y un medicamento");
		}	
		
		if( stock.getInsumosMedicos() != null && stock.getInsumosMedicos().getInsumoMedicoId() != null ) {
			insumoMedico.setInsumoMedicoId(stock.getInsumosMedicos().getInsumoMedicoId());
			stockExistente = stockDao.findByInsumosMedicos(insumoMedico);
			if( stockExistente != null) throw new SigebiException.BusinessException("El insumo médico ya existe en el stock");
			stock.setMedicamentos(null);
		}else if( stock.getMedicamentos() != null && stock.getMedicamentos().getMedicamentoId() != null ) {
			stock.setInsumosMedicos(null);
			medicamento.setMedicamentoId(stock.getMedicamentos().getMedicamentoId());
			stockExistente = stockDao.findByMedicamentos(medicamento);
			if( stockExistente != null) throw new SigebiException.BusinessException("El medicamento ya existe en el stock ");
		}							
		
		//guardar el movimiento de insumos/medicamentos	
		MovimientosInsumos movimientoInsumo = new MovimientosInsumos();
		
		movimientoInsumo.setCantidadEntrada(stock.getCantidad());
		movimientoInsumo.setCodProceso(Globales.Procesos.ALTA_STOCK);
		movimientoInsumo.setInsumosMedicos(stock.getInsumosMedicos());
		movimientoInsumo.setMedicamentos(stock.getMedicamentos());
		movimientoInsumo.setUsuarioCreacion(stock.getUsuarioCreacion());
		
		movimientoInsumoDao.save(movimientoInsumo);
		
		return stockDao.save(stock);
	}
	
	@Override
	@Transactional
	public Stock actualizar(Stock stock) throws SigebiException {
		
		if ( stock.getStockId() == null ) {
			throw new SigebiException.BusinessException("Stock id es requerido ");
		}
		
		Stock stockActual = stockDao.findById(stock.getStockId()).orElse(null);
		
		if ( stockActual == null ) {
			String mensaje = "Error: no se pudo editar, el stock ID: "
					.concat(String.valueOf(stock.getStockId()).concat(" no existe en la base de datos!"));
			throw new SigebiException.DataNotFound(mensaje);
		}
		
		Integer cantidadModificar = stock.getCantidad();
		
		stock.setCantidad(stockActual.getCantidad() + cantidadModificar);
		
		if( stock.getInsumosMedicos() != null && stock.getInsumosMedicos().getInsumoMedicoId() != null ) {
			stock.setMedicamentos(null);
		}else if( stock.getMedicamentos() != null && stock.getMedicamentos().getMedicamentoId() != null ) {
			stock.setInsumosMedicos(null);
		}
		
		//guardar el movimiento de insumos/medicamentos	
		MovimientosInsumos movimientoInsumo = new MovimientosInsumos();
		
		movimientoInsumo.setCantidadEntrada(stock.getCantidad());
		movimientoInsumo.setCodProceso(Globales.Procesos.MODIF_STOCK);
		movimientoInsumo.setInsumosMedicos(stock.getInsumosMedicos());
		movimientoInsumo.setMedicamentos(stock.getMedicamentos());
		movimientoInsumo.setUsuarioCreacion(stock.getUsuarioCreacion());
		
		movimientoInsumoDao.save(movimientoInsumo);
		
		return stockDao.save(stock);
	}

	@Override
	@Transactional
	public void delete(int id) {
		stockDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Stock> buscar(Date fromDate, Date toDate, Stock stock, List<Integer> insumosId, List<Integer> medicamentosId, Pageable pageable) {
		
        List<Stock> stockList = stockDao.findAll((Specification<Stock>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( stock.getStockId() != null ) {
                p = cb.and(p, cb.equal(root.get("stockId"), stock.getStockId()) );
            } 
            if( insumosId != null && !insumosId.isEmpty() ){
            	p = cb.and(root.get("insumosMedicos").in(insumosId));
            } 
            if( medicamentosId != null && !medicamentosId.isEmpty() ){
            	p = cb.and(root.get("medicamentos").in(medicamentosId));
            } 
            cq.orderBy(cb.desc(root.get("stockId")));
            return p;
        }, pageable).getContent();
        
        for(Stock stockData : stockList) {
        	if(stockData.getInsumosMedicos() == null) {
        		stockData.setInsumosMedicos(new InsumosMedicos());
        	}
        	if(stockData.getMedicamentos() == null ) {
        		stockData.setMedicamentos(new Medicamentos());
        	}
        }
        return stockList;
    }

}
