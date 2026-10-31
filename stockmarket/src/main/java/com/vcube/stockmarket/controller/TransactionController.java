package com.vcube.stockmarket.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vcube.stockmarket.entity.Portfolio;
import com.vcube.stockmarket.entity.Stock;
import com.vcube.stockmarket.entity.TransactionType;
import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.PortfolioRepository;
import com.vcube.stockmarket.repository.StockRepository;
import com.vcube.stockmarket.repository.UserRepository;
import com.vcube.stockmarket.service.TransactionService;

import jakarta.transaction.Transaction;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PortfolioRepository portfolioRepo;
    
    @Autowired
    StockRepository stockRepo;

    
    @GetMapping("/transactions")
    public String viewTransactions(Authentication authentication, Model model) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        model.addAttribute("transactions",
                transactionService.getUserTransactions(user));

        return "transactions"; // Thymeleaf template: transactions.html
    }
    @PostMapping("/buy")
    public String buyStock(
            @RequestParam Long stockId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        


        transactionService.buyStock(user, stockId, quantity);
        System.out.println("BUY API HIT");
        System.out.println("StockId = " + stockId + ", Qty = " + quantity);

        return "redirect:/stocks";
    }
    
    @PostMapping("/selling")
    public String sellStock(Authentication authentication,
                            @RequestParam Long stockId,
                            @RequestParam int quantity,
                            RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        transactionService.sellStock(user, stockId, quantity);

        redirectAttributes.addFlashAttribute("success",
                "Stock sold successfully");

        return "redirect:/portfolio";
    }

}
