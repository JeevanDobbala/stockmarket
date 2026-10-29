package com.vcube.stockmarket.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vcube.stockmarket.entity.Portfolio;
import com.vcube.stockmarket.entity.Stock;
import com.vcube.stockmarket.entity.Transaction;
import com.vcube.stockmarket.entity.TransactionType;
import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.PortfolioRepository;
import com.vcube.stockmarket.repository.StockRepository;
import com.vcube.stockmarket.repository.TransactionRepository;
import com.vcube.stockmarket.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TradingService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private StockRepository stockRepo;
	@Autowired
	private PortfolioRepository portfolioRepo;
	@Autowired
	private TransactionRepository transactionRepo;

	
	@Transactional
	public void buyStock(Long userId, Long stockId, int quantity) {

	    User user = userRepo.findById(userId).orElseThrow();
	    Stock stock = stockRepo.findById(stockId).orElseThrow();

	    BigDecimal totalCost = stock.getCurrentPrice()
	            .multiply(BigDecimal.valueOf(quantity));

	    if (user.getWalletBalance().compareTo(totalCost) < 0) {
	        throw new RuntimeException("Insufficient balance");
	    }

	    // Deduct balance
	    user.setWalletBalance(user.getWalletBalance().subtract(totalCost));

	    // Portfolio update
	    Portfolio portfolio = portfolioRepo
	            .findByUserAndStock(user, stock)
	            .orElse(new Portfolio());

	    // ✅ EXTRA SAFETY CODE (ADDED)
	    if (portfolio.getQuantity() == null) {
	        portfolio.setQuantity(0);
	    }

	    portfolio.setUser(user);
	    portfolio.setStock(stock);
	    portfolio.setQuantity(portfolio.getQuantity() + quantity);
	    portfolio.setAveragePrice(stock.getCurrentPrice());

	    portfolioRepo.save(portfolio);

	    // Transaction record
	    Transaction txn = new Transaction();
	    txn.setUser(user);
	    txn.setStock(stock);
	    txn.setTransactionType(TransactionType.BUY);
	    txn.setQuantity(quantity);
	    txn.setPrice(stock.getCurrentPrice());
	    txn.setTransactionDate(LocalDateTime.now());

	    transactionRepo.save(txn);

	    userRepo.save(user);
	}
	
	@Transactional
	public void sellStock(Long userId, Long stockId, int quantity) {

	    User user = userRepo.findById(userId).orElseThrow();
	    Stock stock = stockRepo.findById(stockId).orElseThrow();

	    Portfolio portfolio = portfolioRepo
	            .findByUserAndStock(user, stock)
	            .orElseThrow(() -> new RuntimeException("Stock not in portfolio"));

	    if (portfolio.getQuantity() < quantity) {
	        throw new RuntimeException("Not enough stock quantity to sell");
	    }

	    // Add balance to wallet
	    BigDecimal totalAmount = stock.getCurrentPrice()
	            .multiply(BigDecimal.valueOf(quantity));

	    user.setWalletBalance(user.getWalletBalance().add(totalAmount));

	    // Update portfolio quantity
	    portfolio.setQuantity(portfolio.getQuantity() - quantity);

	    if (portfolio.getQuantity() == 0) {
	        portfolioRepo.delete(portfolio);
	    } else {
	        portfolioRepo.save(portfolio);
	    }

	    // Transaction record
	    Transaction txn = new Transaction();
	    txn.setUser(user);
	    txn.setStock(stock);
	    txn.setTransactionType(TransactionType.SELL);
	    txn.setQuantity(quantity);
	    txn.setPrice(stock.getCurrentPrice());
	    txn.setTransactionDate(LocalDateTime.now());

	    transactionRepo.save(txn);

	    userRepo.save(user);
	}


}
