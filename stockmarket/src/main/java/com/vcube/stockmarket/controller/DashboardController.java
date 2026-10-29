package com.vcube.stockmarket.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vcube.stockmarket.entity.Portfolio;
import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.PortfolioRepository;
import com.vcube.stockmarket.repository.UserRepository;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {

        // Logged-in user email
        String loggedInUserEmail = authentication.getName();
        model.addAttribute("email", loggedInUserEmail);

        // Fetch user
        User user = userRepository.findByEmail(loggedInUserEmail).orElseThrow();

        // Wallet balance
        model.addAttribute("walletBalance", user.getWalletBalance());

        // User portfolio
        List<Portfolio> portfolios = portfolioRepository.findByUser(user);
        model.addAttribute("portfolios", portfolios);

        // Total portfolio value
        BigDecimal totalPortfolioValue = portfolios.stream()
                .map(p -> p.getStock().getCurrentPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalPortfolioValue", totalPortfolioValue);

        // Total Gain / Loss
        BigDecimal totalInvested = portfolios.stream()
                .map(p -> p.getAveragePrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalGainLoss = totalPortfolioValue.subtract(totalInvested);
        model.addAttribute("totalGainLoss", totalGainLoss);

        return "dashboard"; // Thymeleaf template
    }
}