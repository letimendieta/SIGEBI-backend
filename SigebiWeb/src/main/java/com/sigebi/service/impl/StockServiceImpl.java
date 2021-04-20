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

import com.sigebi.dao.IStockDao;
import com.sigebi.entity.Stock;
import com.sigebi.service.StockService;


@Service
public class StockServiceImpl implements StockService{

	@Autowired
	private IStockDao stockDao;
	
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
	public Stock findById(int id) {
		return stockDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Stock save(Stock cliente) {
		return stockDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(int id) {
		stockDao.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Stock> buscar(Date fromDate, Date toDate, Stock stock, List<Integer> insumosId, Pageable pageable) {
		
        List<Stock> stockList = stockDao.findAll((Specification<Stock>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && fromDate.before(toDate)) {
                p = cb.and(p, cb.between(root.get("fechaCreacion"), fromDate, toDate));
            }
            if ( stock.getStockId() != null ) {
                p = cb.and(p, cb.equal(root.get("stockId"), stock.getStockId()) );
            } 
            if( insumosId != null && !insumosId.isEmpty() ){
            	p = cb.and(root.get("insumos").in(insumosId));
            } 
            cq.orderBy(cb.desc(root.get("stockId")));
            return p;
        }, pageable).getContent();
        return stockList;
    }

}
