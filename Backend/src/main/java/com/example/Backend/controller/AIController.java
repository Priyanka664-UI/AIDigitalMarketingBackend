package com.example.Backend.controller;

import com.example.Backend.model.Business;
import com.example.Backend.model.Post;
import com.example.Backend.model.Campaign;
import com.example.Backend.repository.BusinessRepository;
import com.example.Backend.repository.PostRepository;
import com.example.Backend.repository.CampaignRepository;
import com.example.Backend.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private BusinessRepository businessRepository;

    @Value("${openai.api.key:your-openai-api-key-here}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1}")
    private String apiUrl;

    @PostMapping("/generate-content")
    public ResponseEntity<?> generateContent(@RequestBody Map<String, Object> request) {
        try {
            Long campaignId = Long.valueOf(request.get("campaignId").toString());
            Integer days = Integer.valueOf(request.getOrDefault("days", 7).toString());
            String tone = (String) request.getOrDefault("tone", "Professional");
            String platform = (String) request.getOrDefault("platform", "INSTAGRAM");

            Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
            if (campaign == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campaign not found"));
            }

            List<Post> posts = new ArrayList<>();
            String businessName = campaign.getBusiness().getBusinessName();

            for (int i = 0; i < days; i++) {
                Post post = new Post();
                post.setCampaign(campaign);
                post.setPlatform(Post.Platform.valueOf(platform));

                String topic = "Day " + (i + 1) + " marketing content";
                String caption = openAIService.generateCaption(businessName, tone, topic);
                post.setCaption(caption);

                post.setHashtags("#marketing #ai #socialmedia #" + businessName.toLowerCase().replace(" ", ""));
                post.setScheduledTime(LocalDateTime.now().plusDays(i));
                post.setStatus(Post.PostStatus.SCHEDULED);
                posts.add(postRepository.save(post));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Content generated successfully",
                    "posts", posts,
                    "count", posts.size()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/generate-image")
    public ResponseEntity<?> generateImage(@RequestBody Map<String, String> request) {
        try {
            String prompt = request.get("prompt");

            if (prompt == null || prompt.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Prompt is required",
                        "imageUrl", "https://via.placeholder.com/1024x1024?text=No+Prompt"));
            }

            System.out.println("Generating image for: " + prompt);
            String imageUrl = openAIService.generateImage(prompt);
            System.out.println("Generated URL: " + imageUrl);

            return ResponseEntity.ok(Map.of(
                    "imageUrl", imageUrl,
                    "message", "Image generated successfully",
                    "prompt", prompt));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Map.of(
                    "imageUrl", "https://via.placeholder.com/1024x1024?text=" + request.get("prompt").replace(" ", "+"),
                    "message", "Using fallback image",
                    "error", e.getMessage()));
        }
    }

    @PostMapping("/regenerate-caption")
    public ResponseEntity<?> regenerateCaption(@RequestBody Map<String, Object> request) {
        try {
            Long postId = Long.valueOf(request.get("postId").toString());
            Post post = postRepository.findById(postId).orElse(null);

            if (post != null) {
                String businessName = post.getCampaign().getBusiness().getBusinessName();
                String newCaption = openAIService.generateCaption(businessName, "Professional", "social media post");
                post.setCaption(newCaption);
                postRepository.save(post);
                return ResponseEntity.ok(Map.of(
                        "message", "Caption regenerated successfully",
                        "post", post));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/insights")
    public ResponseEntity<?> getInsights(@RequestParam Long businessId) {
        Business business = businessRepository.findById(businessId).orElse(null);
        String businessName = business != null ? business.getBusinessName() : "Small Business";
        String industry = business != null ? business.getIndustry() : "General";

        Map<String, Object> insights = new HashMap<>();
        insights.put("bestContentType", "Image Posts");
        insights.put("bestPostingTime", "10:00 AM - 12:00 PM");
        insights.put("suggestedCampaigns", openAIService.generateSuggestedCampaigns(businessName + " " + industry));
        insights.put("trendingTopics", openAIService.generateTrendingTopics(industry));

        return ResponseEntity.ok(insights);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAI() {
        Map<String, Object> status = new HashMap<>();
        status.put("openAIConfigured",
                apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-openai-api-key-here"));
        status.put("apiUrl", apiUrl);
        status.put("message", "AI service is running");

        if (status.get("openAIConfigured").equals(false)) {
            status.put("warning", "OpenAI API key not configured. Using fallback responses.");
        }

        return ResponseEntity.ok(status);
    }
}
