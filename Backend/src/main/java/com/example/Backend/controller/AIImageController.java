package com.example.Backend.controller;

import com.example.Backend.dto.ImageGenerationRequest;
import com.example.Backend.model.Post;
import com.example.Backend.repository.PostRepository;
import com.example.Backend.service.AIImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai/image")
@RequiredArgsConstructor
public class AIImageController {
    
    private final AIImageService aiImageService;
    private final PostRepository postRepository;
    
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateImage(@RequestBody ImageGenerationRequest request) {
        String imageData = aiImageService.generateImage(request.getPrompt());
        return ResponseEntity.ok(Map.of("imageUrl", imageData));
    }
    
    @PostMapping("/regenerate/{postId}")
    public ResponseEntity<Map<String, String>> regeneratePostImage(@PathVariable Long postId, @RequestBody(required = false) Map<String, String> body) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("Post not found"));
        
        String prompt = body != null && body.containsKey("prompt") 
            ? body.get("prompt") 
            : "Professional marketing image for " + post.getCaption().substring(0, Math.min(50, post.getCaption().length()));
        
        String imageData = aiImageService.generateImage(prompt);
        post.setImageUrl(imageData);
        postRepository.save(post);
        
        return ResponseEntity.ok(Map.of("imageUrl", imageData, "message", "Image regenerated successfully"));
    }
}
