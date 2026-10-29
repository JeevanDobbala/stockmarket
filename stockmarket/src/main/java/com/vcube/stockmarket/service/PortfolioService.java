package com.vcube.stockmarket.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcube.stockmarket.entity.Portfolio;
import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.PortfolioRepository;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepo;

    public List<Portfolio> getUserPortfolio(User user) {
    	List<Portfolio> portfolios = portfolioRepo.findByUser(user);
        for (Portfolio p : portfolios) {
            BigDecimal totalValue = p.getStock().getCurrentPrice()
                                     .multiply(BigDecimal.valueOf(p.getQuantity()));
            p.setTotalValue(totalValue); // add totalValue field in Portfolio entity
        }
        return portfolios;
    }
}