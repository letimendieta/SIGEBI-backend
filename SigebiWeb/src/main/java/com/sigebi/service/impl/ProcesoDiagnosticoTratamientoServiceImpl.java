package com.sigebi.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigebi.dao.IAreasDao;
import com.sigebi.dao.IConsultasDao;
import com.sigebi.dao.IDiagnosticosDao;
import com.sigebi.dao.IStockDao;
import com.sigebi.dao.ITratamientosDao;
import com.sigebi.dao.ITratamientosInsumosDao;
import com.sigebi.entity.Consultas;
import com.sigebi.entity.Insumos;
import com.sigebi.entity.ProcesoDiagnosticoTratamiento;
import com.sigebi.entity.Stock;
import com.sigebi.entity.Tratamientos;
import com.sigebi.entity.TratamientosInsumos;
import com.sigebi.service.ProcesoDiagnosticoTratamientoService;


@Service
public class ProcesoDiagnosticoTratamientoServiceImpl implements ProcesoDiagnosticoTratamientoService{

	@Autowired
	private IDiagnosticosDao diagnosticoDao;	
	@Autowired
	private ITratamientosDao tratamientoDao;
	@Autowired
	private ITratamientosInsumosDao tratamientoInsumoDao;
	@Autowired
	private IConsultasDao consultaDao;
	@Autowired
	private IStockDao stockDao;

	@Override
	@Transactional
	public void save(ProcesoDiagnosticoTratamiento procesoDiagnosticoTratamiento) throws Exception {
		
		//Guardar la consulta
		Consultas consulta;
		try {
			procesoDiagnosticoTratamiento.getConsulta().setFecha(LocalDateTime.now());;
			consulta = consultaDao.save(procesoDiagnosticoTratamiento.getConsulta());
		} catch (Exception e) {
			throw new Exception("Error al guardar el diagnostico " + e.getMessage());
		}
		
		//Guardar diagnostico		
		try {
			procesoDiagnosticoTratamiento.getDiagnostico().setConsultaId(consulta.getConsultaId());
			diagnosticoDao.save(procesoDiagnosticoTratamiento.getDiagnostico());
		} catch (Exception e) {
			throw new Exception("Error al guardar el diagnostico " + e.getMessage());
		}
		
		//Guardar el tratamiento
		Tratamientos tratamiento = null;
		try {
			procesoDiagnosticoTratamiento.getTratamiento().setConsultaId(consulta.getConsultaId());
			tratamiento = tratamientoDao.save(procesoDiagnosticoTratamiento.getTratamiento());
		} catch (Exception e) {
			throw new Exception("Error al guardar el tratamiento " + e.getMessage());
		}
		
		//Guardar el tratamiento insumo
		try {
			for(TratamientosInsumos tratamientoInsumo : procesoDiagnosticoTratamiento.getTratamientoInsumoList()) {
				tratamientoInsumo.setTratamientos(tratamiento);
				tratamientoInsumoDao.save(tratamientoInsumo);
				
				int cantidadUsada = 0;
				if ( tratamientoInsumo.getCantidad() != null ) {
					cantidadUsada = tratamientoInsumo.getCantidad();
				}						
				
				if( cantidadUsada > 0) {
					descontarStock(tratamientoInsumo);
				}
			}			
		} catch (Exception e) {
			throw new Exception("Error al guardar los medicamentos del tratamiento " + e.getMessage());
		}
	}

	public void descontarStock(TratamientosInsumos tratamientoInsumo) throws Exception {
		try {
			Insumos insumo = new Insumos();
			insumo.setInsumoId(tratamientoInsumo.getInsumos().getInsumoId());
			Stock stockAdescontar = stockDao.findByInsumos(insumo);
			
			int cantidadActual = stockAdescontar.getCantidad();
			int cantidadUsada = tratamientoInsumo.getCantidad();
			
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
			// TODO: handle exception
		}
	}
	

}
