package com.vcube.stockmarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.UserRepository;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Thymeleaf template
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Default wallet balance
        user.setWalletBalance(java.math.BigDecimal.ZERO);
        // Default role
        user.setRole("USER");
        userRepository.save(user);

        return "login"; // after registration
    }
}
