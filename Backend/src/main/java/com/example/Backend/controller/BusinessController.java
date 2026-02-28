package com.example.Backend.controller;

import com.example.Backend.model.Business;
import com.example.Backend.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/business")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BusinessController {
    
    private final BusinessRepository businessRepository;
    
    @PostMapping
    public ResponseEntity<Business> createBusiness(@RequestBody Business business) {
        return ResponseEntity.ok(businessRepository.save(business));
    }
    
    @GetMapping
    public ResponseEntity<List<Business>> getAllBusinesses() {
        return ResponseEntity.ok(businessRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Business> getBusiness(@PathVariable Long id) {
        return businessRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Business> updateBusiness(@PathVariable Long id, @RequestBody Business business) {
        return businessRepository.findById(id)
            .map(existing -> {
                business.setId(id);
                return ResponseEntity.ok(businessRepository.save(business));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
