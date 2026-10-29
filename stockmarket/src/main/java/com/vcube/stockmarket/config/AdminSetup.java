package com.vcube.stockmarket.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vcube.stockmarket.entity.User;
import com.vcube.stockmarket.repository.UserRepository;

@Configuration
public class AdminSetup {

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByEmail("admin@tradex.com").isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@tradex.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setWalletBalance(BigDecimal.ZERO);
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("✅ Admin user created!");
            }
        };
    }
} 
