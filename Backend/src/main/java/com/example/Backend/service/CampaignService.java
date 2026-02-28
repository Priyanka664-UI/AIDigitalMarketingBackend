package com.example.Backend.service;

import com.example.Backend.model.*;
import com.example.Backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final PostRepository postRepository;
    private final BusinessRepository businessRepository;
    private final AIContentService aiContentService;

    public Campaign createCampaign(Long businessId, String name, LocalDate startDate, LocalDate endDate) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));

        Campaign campaign = new Campaign();
        campaign.setBusiness(business);
        campaign.setName(name);
        campaign.setStartDate(startDate);
        campaign.setEndDate(endDate);
        campaign.setStatus(CampaignStatus.DRAFT);

        return campaignRepository.save(campaign);
    }

    public List<Post> generateContentCalendar(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        Business business = campaign.getBusiness();
        List<Post> posts = new ArrayList<>();

        LocalDate currentDate = campaign.getStartDate();
        Post.Platform[] platforms = Post.Platform.values();
        int platformIndex = 0;

        while (!currentDate.isAfter(campaign.getEndDate())) {
            // Generate 3 posts per week
            if (currentDate.getDayOfWeek().getValue() == 1 ||
                    currentDate.getDayOfWeek().getValue() == 3 ||
                    currentDate.getDayOfWeek().getValue() == 5) {

                Post.Platform platform = platforms[platformIndex % platforms.length];
                Post post = createPost(campaign, business, platform, currentDate);
                posts.add(postRepository.save(post));
                platformIndex++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return posts;
    }

    private Post createPost(Campaign campaign, Business business, Post.Platform platform, LocalDate date) {
        Post post = new Post();
        post.setCampaign(campaign);
        post.setPlatform(platform);
        post.setCaption(aiContentService.generateCaption(business, platform));
        post.setHashtags(aiContentService.generateHashtags(business, platform));
        post.setImageUrl("https://via.placeholder.com/800x600?text=" + business.getBusinessName());
        post.setScheduledTime(date.atTime(10, 0));
        post.setStatus(Post.PostStatus.SCHEDULED);

        // Simulate engagement metrics
        post.setLikes(new Random().nextInt(500));
        post.setShares(new Random().nextInt(100));
        post.setComments(new Random().nextInt(50));
        post.setReach(new Random().nextInt(2000));

        return post;
    }

    public List<Campaign> getCampaignsByBusiness(Long businessId) {
        return campaignRepository.findByBusinessId(businessId);
    }

    public List<Post> getPostsByCampaign(Long campaignId) {
        return postRepository.findByCampaignId(campaignId);
    }

    public Map<String, Object> getCampaignAnalytics(Long campaignId) {
        List<Post> posts = postRepository.findByCampaignId(campaignId);

        int totalLikes = posts.stream().mapToInt(p -> p.getLikes() != null ? p.getLikes() : 0).sum();
        int totalShares = posts.stream().mapToInt(p -> p.getShares() != null ? p.getShares() : 0).sum();
        int totalComments = posts.stream().mapToInt(p -> p.getComments() != null ? p.getComments() : 0).sum();
        int totalReach = posts.stream().mapToInt(p -> p.getReach() != null ? p.getReach() : 0).sum();

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalPosts", posts.size());
        analytics.put("totalLikes", totalLikes);
        analytics.put("totalShares", totalShares);
        analytics.put("totalComments", totalComments);
        analytics.put("totalReach", totalReach);
        analytics.put("engagementRate", posts.isEmpty() ? 0 : (totalLikes + totalComments) * 100.0 / totalReach);

        return analytics;
    }
}
