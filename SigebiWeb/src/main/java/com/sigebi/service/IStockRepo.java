package com.sigebi.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sigebi.model.Stock;

public interface IStockRepo extends JpaRepository<Stock, Integer> {

}
