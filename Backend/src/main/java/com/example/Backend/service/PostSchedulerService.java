package com.example.Backend.service;

import com.example.Backend.dto.PostScheduleRequest;
import com.example.Backend.model.Campaign;
import com.example.Backend.model.Post;
import com.example.Backend.repository.CampaignRepository;
import com.example.Backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PostSchedulerService {
    
    private final PostRepository postRepository;
    private final CampaignRepository campaignRepository;
    
    public Post schedulePost(PostScheduleRequest request) {
        if (request.getCampaignId() == null || request.getCampaignId() == 0) {
            throw new RuntimeException("Campaign ID is required. Please select a campaign.");
        }
        
        Campaign campaign = campaignRepository.findById(request.getCampaignId())
            .orElseThrow(() -> new RuntimeException("Campaign not found with ID: " + request.getCampaignId()));
        
        Post post = new Post();
        post.setCampaign(campaign);
        post.setPlatform(Post.Platform.valueOf(request.getPlatform().toUpperCase()));
        post.setCaption(request.getCaption());
        post.setHashtags(request.getHashtags());
        post.setImageUrl(request.getImageUrl());
        post.setScheduledTime(request.getScheduledTime());
        post.setStatus(Post.PostStatus.SCHEDULED);
        
        return postRepository.save(post);
    }
    
    public List<Post> getScheduledPosts(Long campaignId) {
        return postRepository.findByCampaignIdAndStatus(campaignId, Post.PostStatus.SCHEDULED);
    }
    
    public Post updatePost(Long postId, PostScheduleRequest request) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        
        post.setCaption(request.getCaption());
        post.setHashtags(request.getHashtags());
        post.setImageUrl(request.getImageUrl());
        post.setScheduledTime(request.getScheduledTime());
        
        return postRepository.save(post);
    }
    
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
    
    // Cron job to auto-publish posts (runs every minute)
    @Scheduled(fixedRate = 60000)
    public void autoPublishPosts() {
        LocalDateTime now = LocalDateTime.now();
        List<Post> scheduledPosts = postRepository.findByStatusAndScheduledTimeBefore(
            Post.PostStatus.SCHEDULED, now
        );
        
        for (Post post : scheduledPosts) {
            publishPost(post);
        }
    }
    
    private void publishPost(Post post) {
        // Simulate API call to social media platform
        boolean success = simulateSocialMediaAPI(post);
        
        if (success) {
            post.setStatus(Post.PostStatus.PUBLISHED);
            // Simulate engagement metrics
            Random random = new Random();
            post.setLikes(random.nextInt(500) + 50);
            post.setComments(random.nextInt(50) + 5);
            post.setShares(random.nextInt(100) + 10);
            post.setReach(random.nextInt(5000) + 500);
        } else {
            post.setStatus(Post.PostStatus.FAILED);
        }
        
        postRepository.save(post);
    }
    
    private boolean simulateSocialMediaAPI(Post post) {
        // Simulate API call success (95% success rate)
        return new Random().nextInt(100) < 95;
    }
}
