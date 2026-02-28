package com.example.Backend.controller;

import com.example.Backend.dto.ImageGenerationRequest;
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
    
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateImage(@RequestBody ImageGenerationRequest request) {
        String imageData = aiImageService.generateImage(request.getPrompt());
        return ResponseEntity.ok(Map.of("imageUrl", imageData));
    }
}
