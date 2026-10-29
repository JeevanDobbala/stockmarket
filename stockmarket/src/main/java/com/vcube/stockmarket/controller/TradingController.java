package com.vcube.stockmarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.UserRepository;
import com.vcube.stockmarket.service.TradingService;

@Controller
public class TradingController {

    @Autowired
    private TradingService tradingService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/buy/{stockId}")
    public String buyStock(
            @PathVariable Long stockId,
            @RequestParam int quantity,
            Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        tradingService.buyStock(user.getUserId(), stockId, quantity);

        return "redirect:/portfolio";
    }
    
    @PostMapping("/sell")
    public String sellStock(@RequestParam Long stockId,
                             @RequestParam int quantity,
                             Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        tradingService.sellStock(user.getUserId(), stockId, quantity);

        return "redirect:/portfolio";
    }

}
