package com.vcube.stockmarket.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class TransactionService {

	@Autowired
	private StockRepository stockRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PortfolioRepository portfolioRepo;

	@Autowired
	private TransactionRepository transactionRepo;


    public List<Transaction> getUserTransactions(User user) {
        return transactionRepo.findByUserOrderByTransactionDateDesc(user);
    }
    
    @Transactional
    public void buyStock(User user, Long stockId, Integer quantity) {

        // 1. Stock fetch
        Stock stock = stockRepo.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        // 2. Total cost
        BigDecimal totalCost = stock.getCurrentPrice()
                .multiply(BigDecimal.valueOf(quantity));

        // 3. Wallet balance check
        if (user.getWalletBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        // 4. Deduct wallet balance
        user.setWalletBalance(user.getWalletBalance().subtract(totalCost));
        userRepo.save(user);

        // 5. Portfolio update / insert
        Portfolio portfolio = portfolioRepo
                .findByUserAndStock(user, stock)
                .orElse(new Portfolio());

        if (portfolio.getQuantity() == null) {
            portfolio.setQuantity(0);
        }

        portfolio.setUser(user);
        portfolio.setStock(stock);
        portfolio.setQuantity(portfolio.getQuantity() + quantity);
        portfolio.setAveragePrice(stock.getCurrentPrice());

        portfolioRepo.save(portfolio);

        // 6. Transaction entry
        Transaction txn = new Transaction();
        txn.setUser(user);
        txn.setStock(stock);
        txn.setTransactionType(TransactionType.BUY);
        txn.setQuantity(quantity);
        txn.setPrice(stock.getCurrentPrice());
        txn.setTransactionDate(LocalDateTime.now());

        transactionRepo.save(txn);
    }
    
    
    @Transactional
    public void sellStock(User user, Long stockId, int quantityToSell) {
    	
    	User u = userRepo.findById(user.getUserId())
    	        .orElseThrow(() -> new RuntimeException("User not found"));

    	// 1. Portfolio fetch
    	Portfolio portfolio = portfolioRepo.findByUserIdAndStockId(user.getUserId(), stockId)
    	        .orElseThrow(() -> new RuntimeException("Stock not found in portfolio"));

    	if (quantityToSell > portfolio.getQuantity()) {
    	    throw new RuntimeException("Not enough quantity to sell");
    	}

    	BigDecimal sellPrice = portfolio.getStock().getCurrentPrice();
    	BigDecimal buyPrice  = portfolio.getAveragePrice();

    	// 1️⃣ Profit / Loss calculation
    	BigDecimal profitOrLoss = sellPrice
    	        .subtract(buyPrice)
    	        .multiply(BigDecimal.valueOf(quantityToSell));

    	// 2️⃣ Wallet update (correct)
    	BigDecimal sellAmount = sellPrice.multiply(BigDecimal.valueOf(quantityToSell));
    	user.setWalletBalance(user.getWalletBalance().add(sellAmount));
    	userRepo.save(user);

    	// 3. Quantity update
    	portfolio.setQuantity(portfolio.getQuantity() - quantityToSell);

    	if (portfolio.getQuantity() == 0) {
    	    portfolioRepo.delete(portfolio);
    	} else {
    	    portfolioRepo.save(portfolio);
    	}

    	// ❌ OLD
    	// BigDecimal sellMoney = portfolio.getAveragePrice();

    	// ✅ FIX
    	BigDecimal sellMoney = sellPrice;

    	// 4. Transaction
    	Transaction transaction = new Transaction();
    	transaction.setUser(user);
    	transaction.setStock(portfolio.getStock());
    	transaction.setPrice(sellMoney);
    	transaction.setQuantity(quantityToSell);
    	transaction.setTransactionType(TransactionType.SELL);
    	transaction.setTransactionDate(LocalDateTime.now());

    	// ⭐ ADD THIS LINE (only addition)
    	transaction.setProfitOrLoss(profitOrLoss);

    	transactionRepo.save(transaction);

}}