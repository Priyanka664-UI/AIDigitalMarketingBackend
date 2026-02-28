package com.example.Backend.controller;

import com.example.Backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/engagement")
    public ResponseEntity<?> getEngagement(@RequestParam Long businessId) {
        Map<String, Object> engagement = new HashMap<>();
        engagement.put("reachGraph", Arrays.asList(1200, 1500, 1800, 2100, 2400, 2700, 3000));
        engagement.put("totalLikes", 5420);
        engagement.put("totalComments", 892);
        engagement.put("clickThroughRate", 3.5);
        engagement.put("engagementTrend", Arrays.asList(2.1, 2.5, 2.8, 3.2, 3.5, 3.8, 4.1));
        return ResponseEntity.ok(engagement);
    }

    @GetMapping("/platform-comparison")
    public ResponseEntity<?> getPlatformComparison(@RequestParam Long businessId) {
        Map<String, Object> comparison = new HashMap<>();
        
        Map<String, Integer> instagram = new HashMap<>();
        instagram.put("posts", 45);
        instagram.put("reach", 12500);
        instagram.put("engagement", 1250);
        
        Map<String, Integer> facebook = new HashMap<>();
        facebook.put("posts", 38);
        facebook.put("reach", 9800);
        facebook.put("engagement", 890);
        
        comparison.put("instagram", instagram);
        comparison.put("facebook", facebook);
        comparison.put("contentTypePerformance", Map.of("images", 65, "videos", 25, "carousels", 10));
        comparison.put("audienceGrowth", Arrays.asList(100, 150, 220, 310, 420, 550, 700));
        
        return ResponseEntity.ok(comparison);
    }

    @GetMapping("/campaign-performance")
    public ResponseEntity<?> getCampaignPerformance(@RequestParam Long campaignId) {
        Map<String, Object> performance = new HashMap<>();
        performance.put("totalPosts", 30);
        performance.put("totalReach", 25000);
        performance.put("totalEngagement", 2500);
        performance.put("roi", 250.5);
        performance.put("conversionRate", 4.2);
        performance.put("avgEngagementRate", 10.0);
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<?> getDashboardStats(@RequestParam Long businessId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPostsThisMonth", 45);
        stats.put("draftPosts", 12);
        stats.put("followersGroup", "25.4K");
        stats.put("totalReach", "128K");
        stats.put("scheduledPosts", 18);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/report/monthly")
    public ResponseEntity<?> getMonthlyReport(@RequestParam Long businessId) {
        Map<String, Object> report = new HashMap<>();
        report.put("month", "December 2024");
        report.put("totalPosts", 45);
        report.put("totalReach", 128000);
        report.put("totalEngagement", 12800);
        report.put("topPost", "Holiday Campaign Post");
        report.put("downloadUrl", "/reports/monthly-dec-2024.pdf");
        return ResponseEntity.ok(report);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<?> exportCSV(@RequestParam Long businessId) {
        Map<String, String> response = new HashMap<>();
        response.put("downloadUrl", "/exports/analytics-" + businessId + ".csv");
        response.put("message", "CSV export ready");
        return ResponseEntity.ok(response);
    }
}
