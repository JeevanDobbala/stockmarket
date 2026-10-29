package com.vcube.stockmarket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcube.stockmarket.entity.Stock;
import com.vcube.stockmarket.repository.StockRepository;

@Service
public class StockService {

	@Autowired
	private StockRepository stockRepository;

	public List<Stock> getAllStocks() {
		return stockRepository.findAll();
	}
}