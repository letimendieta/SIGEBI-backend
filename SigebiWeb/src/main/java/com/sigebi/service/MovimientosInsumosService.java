package com.sigebi.service;

import com.sigebi.entity.MovimientosInsumos;

public interface MovimientosInsumosService {
	
	//public List<MovimientosInsumos> listar() throws SigebiException;
	
	//public int count();
	
	//public MovimientosInsumos obtener(int id) throws SigebiException;
	
	public MovimientosInsumos guardar(MovimientosInsumos movIns);
	
	//public MovimientosInsumos actualizar(MovimientosInsumos movIns) throws SigebiException;
	
	/*public void eliminar(int id) throws SigebiException;
	
	public List<MovimientosInsumos> buscar(Date fromDate, Date toDate, MovimientosInsumos movIns, 
			String orderBy, String orderDir, Pageable pageable) throws DataAccessException;*/
	
	
}
