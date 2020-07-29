package com.sigebi.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sigebi.entity.Stock;

public interface StockService {
	
	public List<Stock> findAll();
	
	public Stock findById(int id);
	
	public Stock save(Stock stock);
	
	public void delete(int id);
	
	public List<Stock> buscar(Date fromDate, Date toDate, Stock stock, List<Integer> insumosId, Pageable pageable);
	
	
}
