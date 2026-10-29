package com.vcube.stockmarket.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.UserRepository;

@Controller
public class WalletController {

    @Autowired
    private UserRepository userRepository;

    // 1️⃣ Show Add Money Form
    @GetMapping("/wallet/add")
    public String showAddMoneyForm() {
        return "add-money";
    }

    // 2️⃣ Handle Add Money POST
    @PostMapping("/wallet/add")
    public String addMoney(@RequestParam BigDecimal amount, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        user.setWalletBalance(user.getWalletBalance().add(amount));
        userRepository.save(user);

        return "redirect:/dashboard"; // back to dashboard after adding
    }
}