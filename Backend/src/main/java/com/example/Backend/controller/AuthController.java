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
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }

        User savedUser = userRepository.save(user);

        // Create Business entity from user data
        Business business = new Business();
        business.setBusinessName(savedUser.getBusinessName());
        business.setIndustry(savedUser.getCategory());
        business.setTargetAudience(savedUser.getTargetAudience());
        business.setBrandTone(Business.BrandTone.valueOf(savedUser.getBrandTone().toUpperCase()));
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
