package com.sigebi.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.entity.Stock;

public interface IStockDao extends JpaRepository<Stock, Integer> {

}
