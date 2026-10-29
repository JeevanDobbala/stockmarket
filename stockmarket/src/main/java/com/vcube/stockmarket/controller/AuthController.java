package com.vcube.stockmarket.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/welcome")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/postLogin")
    public String postLogin(Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return "redirect:/admin/stocks";
        }
        return "redirect:/dashboard";
    }

}
