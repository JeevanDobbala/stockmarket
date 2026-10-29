package com.vcube.stockmarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.UserRepository;
import com.vcube.stockmarket.service.PortfolioService;
@Controller
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/portfolio")
    public String viewPortfolio(Authentication authentication, Model model) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        model.addAttribute("portfolios",
                portfolioService.getUserPortfolio(user));

        return "portfolio";
    }
}
