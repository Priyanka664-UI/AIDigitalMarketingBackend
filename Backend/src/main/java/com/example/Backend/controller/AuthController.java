package com.example.Backend.controller;

import com.example.Backend.model.Business;
import com.example.Backend.model.User;
import com.example.Backend.repository.BusinessRepository;
import com.example.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }
            
            if (userRepository.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
            }

            User savedUser = userRepository.save(user);

            // Create Business entity from user data
            Business business = new Business();
            business.setBusinessName(savedUser.getBusinessName() != null ? savedUser.getBusinessName() : "Default Business");
            business.setIndustry(savedUser.getCategory() != null ? savedUser.getCategory() : "General");
            business.setTargetAudience(savedUser.getTargetAudience() != null ? savedUser.getTargetAudience() : "General Audience");
            
            // Handle brandTone conversion safely
            if (savedUser.getBrandTone() != null && !savedUser.getBrandTone().isEmpty()) {
                try {
                    business.setBrandTone(Business.BrandTone.valueOf(savedUser.getBrandTone().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    business.setBrandTone(Business.BrandTone.PROFESSIONAL);
                }
            } else {
                business.setBrandTone(Business.BrandTone.PROFESSIONAL);
            }
            
            Business savedBusiness = businessRepository.save(business);

            // Link business to user
            savedUser.setBusinessId(savedBusiness.getId());
            userRepository.save(savedUser);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", savedUser.getId());
            response.put("businessId", savedBusiness.getId());
            response.put("token", "dummy-jwt-token");
            response.put("message", "Registration successful");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("businessId", user.getBusinessId() != null ? user.getBusinessId() : user.getId());
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }
}
