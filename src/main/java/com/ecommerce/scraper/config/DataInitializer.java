package com.ecommerce.scraper.config;

import com.ecommerce.scraper.model.User;
import com.ecommerce.scraper.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserService userService;
    
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        try {
            userService.createUser("admin", "admin@example.com", "admin123");
            System.out.println("Default admin user created - Username: admin, Password: admin123");
        } catch (Exception e) {
            // User already exists, ignore
        }
    }
}