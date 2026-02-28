package com.example.Backend.controller;

import com.example.Backend.model.Campaign;
import com.example.Backend.model.Post;
import com.example.Backend.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CampaignController {
    
    private final CampaignService campaignService;
    
    @PostMapping
    public ResponseEntity<Campaign> createCampaign(@RequestBody CampaignRequest request) {
        Campaign campaign = campaignService.createCampaign(
            request.businessId(),
            request.name(),
            request.startDate(),
            request.endDate()
        );
        return ResponseEntity.ok(campaign);
    }
    
    @GetMapping("/business/{businessId}")
    public ResponseEntity<List<Campaign>> getCampaignsByBusiness(@PathVariable Long businessId) {
        return ResponseEntity.ok(campaignService.getCampaignsByBusiness(businessId));
    }
    
    @PostMapping("/{campaignId}/generate-content")
    public ResponseEntity<List<Post>> generateContent(@PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignService.generateContentCalendar(campaignId));
    }
    
    @GetMapping("/{campaignId}/posts")
    public ResponseEntity<List<Post>> getCampaignPosts(@PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignService.getPostsByCampaign(campaignId));
    }
    
    @GetMapping("/{campaignId}/analytics")
    public ResponseEntity<Map<String, Object>> getCampaignAnalytics(@PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignService.getCampaignAnalytics(campaignId));
    }
    
    record CampaignRequest(Long businessId, String name, LocalDate startDate, LocalDate endDate) {}
}
