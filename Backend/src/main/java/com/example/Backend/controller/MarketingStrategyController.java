package com.example.Backend.controller;

import com.example.Backend.dto.MarketingStrategyRequest;
import com.example.Backend.service.MarketingStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/marketing-strategy")
@CrossOrigin(origins = "*")
public class MarketingStrategyController {

    @Autowired
    private MarketingStrategyService marketingStrategyService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateStrategy(@RequestBody MarketingStrategyRequest request) {
        try {
            if (request.getBusinessName() == null || request.getBusinessCategory() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Business name and category are required"
                ));
            }

            String strategy = marketingStrategyService.generateMarketingStrategy(request);
            
            return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(strategy);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Failed to generate strategy: " + e.getMessage()
            ));
        }
    }
}
