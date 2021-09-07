package com.sigebi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IMovimientoInsumoDao;
import com.sigebi.entity.MovimientosInsumos;
import com.sigebi.service.MovimientosInsumosService;


@Service
public class MovimientosInsumosServiceImpl implements MovimientosInsumosService{

	@Autowired
	private IMovimientoInsumoDao movimientoInsumoDao;
	
	
	public MovimientosInsumosServiceImpl(IMovimientoInsumoDao movimientoInsumoDao) {
        this.movimientoInsumoDao = movimientoInsumoDao;
    }
		
	@Override
	@Transactional
	public MovimientosInsumos guardar(MovimientosInsumos movimientoInsumo) {
		return movimientoInsumoDao.save(movimientoInsumo);
	}
	

}
