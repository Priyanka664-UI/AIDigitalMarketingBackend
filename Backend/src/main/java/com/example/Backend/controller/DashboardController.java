package com.example.Backend.controller;

import com.example.Backend.model.Business;
import com.example.Backend.model.Post;
import com.example.Backend.repository.BusinessRepository;
import com.example.Backend.repository.PostRepository;
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

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats(@RequestParam Long businessId) {
        List<Post> posts = postRepository.findByCampaignBusinessId(businessId);
        Business business = businessRepository.findById(businessId).orElse(null);

        Map<String, Object> stats = new HashMap<>();

        long count = posts.size();
        long publishedCount = posts.stream().filter(p -> p.getStatus() == Post.PostStatus.PUBLISHED).count();
        long scheduled = posts.stream().filter(p -> p.getStatus() == Post.PostStatus.SCHEDULED).count();
        long totalReach = posts.stream().mapToLong(p -> p.getReach() != null ? p.getReach() : 0).sum();
        long totalEngagement = posts.stream().mapToLong(p -> (p.getLikes() != null ? p.getLikes() : 0) +
                (p.getComments() != null ? p.getComments() : 0)).sum();

        double avgEngagementRate = totalReach > 0 ? (double) totalEngagement / totalReach * 100 : 0;

        stats.put("totalPosts", count);
        stats.put("totalReach", formatReach(totalReach));
        stats.put("avgEngagementRate", Math.round(avgEngagementRate * 10.0) / 10.0);
        stats.put("scheduledPosts", scheduled);
        stats.put("draftPosts", 0);
        stats.put("aiCredits", business != null ? business.getAiCredits() : 100);
        stats.put("creditsResetDays", business != null ? business.getCreditsResetDays() : 30);

        // Trends
        stats.put("postsTrend", count > 0 ? "+12%" : "0%");
        stats.put("engagementTrend", totalEngagement > 0 ? "+0.5%" : "0%");

        return ResponseEntity.ok(stats);
    }

    private String formatReach(long reach) {
        if (reach == 0)
            return "0";
        if (reach >= 1000000)
            return String.format("%.1fM", reach / 1000000.0);
        if (reach >= 1000)
            return String.format("%.1fK", reach / 1000.0);
        return String.valueOf(reach);
    }

    @GetMapping("/platform-status")
    public ResponseEntity<?> getPlatformStatus(@RequestParam Long businessId) {
        List<Post> posts = postRepository.findByCampaignBusinessId(businessId);
        Set<Post.Platform> activePlatforms = new HashSet<>();
        posts.forEach(p -> activePlatforms.add(p.getPlatform()));

        List<Map<String, Object>> platforms = new ArrayList<>();

        for (Post.Platform p : Post.Platform.values()) {
            Map<String, Object> platformMap = new HashMap<>();
            platformMap.put("name", capitalize(p.name()));
            platformMap.put("connected", activePlatforms.contains(p));
            platformMap.put("icon", getPlatformIcon(p));
            platforms.add(platformMap);
        }

        return ResponseEntity.ok(platforms);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty())
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private String getPlatformIcon(Post.Platform platform) {
        switch (platform) {
            case INSTAGRAM:
                return "lucide:instagram";
            case FACEBOOK:
                return "lucide:facebook";
            case LINKEDIN:
                return "lucide:linkedin";
            case WHATSAPP:
                return "lucide:message-square";
            default:
                return "lucide:share-2";
        }
    }
}
