package com.example.Backend.service;

import com.example.Backend.model.Post;
import com.example.Backend.model.SocialConnection;
import com.example.Backend.repository.PostRepository;
import com.example.Backend.repository.SocialConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialMediaService {
    
    private final SocialConnectionRepository connectionRepository;
    private final PostRepository postRepository;
    private final Random random = new Random();
    
    @Transactional
    public SocialConnection connectPlatform(Long businessId, Post.Platform platform) {
        var existing = connectionRepository.findByBusinessIdAndPlatform(businessId, platform);
        
        if (existing.isPresent()) {
            var connection = existing.get();
            connection.setIsConnected(true);
            connection.setConnectedAt(LocalDateTime.now());
            connection.setExpiresAt(LocalDateTime.now().plusDays(60));
            return connectionRepository.save(connection);
        }
        
        SocialConnection connection = new SocialConnection();
        connection.setBusinessId(businessId);
        connection.setPlatform(platform);
        connection.setAccessToken("demo_token_" + UUID.randomUUID().toString());
        connection.setIsConnected(true);
        connection.setConnectedAt(LocalDateTime.now());
        connection.setExpiresAt(LocalDateTime.now().plusDays(60));
        
        log.info("Simulated {} connection for business {}", platform, businessId);
        return connectionRepository.save(connection);
    }
    
    @Transactional
    public void disconnectPlatform(Long businessId, Post.Platform platform) {
        connectionRepository.findByBusinessIdAndPlatform(businessId, platform)
            .ifPresent(conn -> {
                conn.setIsConnected(false);
                connectionRepository.save(conn);
            });
    }
    
    public List<SocialConnection> getConnections(Long businessId) {
        return connectionRepository.findByBusinessId(businessId);
    }
    
    @Scheduled(fixedRate = 60000) // Every minute
    @Transactional
    public void processScheduledPosts() {
        LocalDateTime now = LocalDateTime.now();
        List<Post> scheduledPosts = postRepository.findByStatusAndScheduledTimeBefore(
            Post.PostStatus.SCHEDULED, now
        );
        
        for (Post post : scheduledPosts) {
            try {
                simulatePosting(post);
            } catch (Exception e) {
                log.error("Failed to post: {}", e.getMessage());
                post.setStatus(Post.PostStatus.FAILED);
                postRepository.save(post);
            }
        }
    }
    
    private void simulatePosting(Post post) {
        // Simulate posting delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate engagement metrics
        post.setLikes(random.nextInt(500) + 100);
        post.setComments(random.nextInt(50) + 10);
        post.setShares(random.nextInt(100) + 20);
        post.setReach(random.nextInt(5000) + 1000);
        post.setStatus(Post.PostStatus.PUBLISHED);
        
        postRepository.save(post);
        log.info("✅ Simulated posting to {} - Post ID: {}", post.getPlatform(), post.getId());
    }
}
