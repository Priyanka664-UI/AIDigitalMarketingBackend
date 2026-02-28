package com.example.Backend.controller;

import com.example.Backend.model.Business;
import com.example.Backend.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private BusinessRepository businessRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats(@RequestParam Long businessId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPostsThisMonth", 45);
        stats.put("draftPosts", 12);
        stats.put("followersGroup", "25.4K");
        stats.put("totalReach", "128K");
        stats.put("scheduledPosts", 18);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/platform-status")
    public ResponseEntity<?> getPlatformStatus(@RequestParam Long businessId) {
        Business business = businessRepository.findById(businessId).orElse(null);
        
        List<Map<String, Object>> platforms = new ArrayList<>();
        
        Map<String, Object> instagram = new HashMap<>();
        instagram.put("name", "Instagram");
        instagram.put("icon", "📷");
        instagram.put("connected", true);
        platforms.add(instagram);
        
        Map<String, Object> facebook = new HashMap<>();
        facebook.put("name", "Facebook");
        facebook.put("icon", "👥");
        facebook.put("connected", true);
        platforms.add(facebook);
        
        Map<String, Object> linkedin = new HashMap<>();
        linkedin.put("name", "LinkedIn");
        linkedin.put("icon", "💼");
        linkedin.put("connected", false);
        platforms.add(linkedin);
        
        Map<String, Object> whatsapp = new HashMap<>();
        whatsapp.put("name", "WhatsApp");
        whatsapp.put("icon", "💬");
        whatsapp.put("connected", false);
        platforms.add(whatsapp);
        
        return ResponseEntity.ok(platforms);
    }
}
