package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Stock;
import com.sigebi.util.exceptions.SigebiException;

public interface StockService {
	
	public List<Stock> findAll();
	
	public Stock obtener(int id) throws SigebiException;
	
	public Stock save(Stock stock) throws SigebiException;
	
	public Stock actualizar(Stock stock) throws SigebiException;
	
	public void delete(int id);
	
	public List<Stock> buscar(Date fromDate, Date toDate, Stock stock, List<Integer> insumosId, List<Integer> medicamentosId, Pageable pageable);
	
	
}
