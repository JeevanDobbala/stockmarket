package com.vcube.stockmarket.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.UserRepository;
import com.vcube.stockmarket.service.StockService;
import com.vcube.stockmarket.service.TransactionService;

@Controller
public class StockController {

	@Autowired
	private StockService stockService;
	
	@Autowired
	TransactionService transactionService;
      
	
	@Autowired
	private UserRepository userRepo;

	
	
	@GetMapping("/stocks")
	public String viewStocks(Model model) {
		model.addAttribute("stocks", stockService.getAllStocks());
		return "stocks";
	}
	
	@PostMapping("/stocks/buy")
	public String buyStock(
	        @RequestParam Long stockId,
	        @RequestParam Integer quantity,
	        Principal principal) {

		 // Logged-in user fetch
	    User loggedInUser = userRepo.findByEmail(principal.getName())
	            .orElseThrow(() -> new RuntimeException("User not found"));


	    // 2. BUY logic call
	    transactionService.buyStock(loggedInUser, stockId, quantity);

	    // 3. Redirect back (dashboard / portfolio / stocks)
	    return "redirect:/portfolio";
	}

}