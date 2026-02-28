package com.example.Backend.controller;

import com.example.Backend.dto.*;
import com.example.Backend.model.Post;
import com.example.Backend.service.ContentManagementService;
import com.example.Backend.service.PostSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentManagementController {
    
    private final ContentManagementService contentManagementService;
    private final PostSchedulerService postSchedulerService;
    
    @PostMapping("/generate")
    public ResponseEntity<ContentGenerationResponse> generateContent(
            @RequestBody ContentGenerationRequest request) {
        return ResponseEntity.ok(contentManagementService.generateContent(request));
    }
    
    @PostMapping("/creative/generate")
    public ResponseEntity<Map<String, String>> generateCreative(
            @RequestBody CreativeGenerationRequest request) {
        return ResponseEntity.ok(contentManagementService.generateCreative(request));
    }
    
    @PostMapping("/schedule")
    public ResponseEntity<?> schedulePost(@RequestBody PostScheduleRequest request) {
        try {
            return ResponseEntity.ok(postSchedulerService.schedulePost(request));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/scheduled/{campaignId}")
    public ResponseEntity<List<Post>> getScheduledPosts(@PathVariable Long campaignId) {
        return ResponseEntity.ok(postSchedulerService.getScheduledPosts(campaignId));
    }
    
    @PutMapping("/post/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostScheduleRequest request) {
        return ResponseEntity.ok(postSchedulerService.updatePost(postId, request));
    }
    
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postSchedulerService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
