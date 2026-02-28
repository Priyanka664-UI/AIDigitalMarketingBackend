package com.example.Backend.controller;

import com.example.Backend.model.Post;
import com.example.Backend.model.SocialConnection;
import com.example.Backend.service.SocialMediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SocialMediaController {
    
    private final SocialMediaService socialMediaService;
    
    @PostMapping("/connect")
    public ResponseEntity<SocialConnection> connectPlatform(@RequestBody ConnectRequest request) {
        SocialConnection connection = socialMediaService.connectPlatform(
            request.businessId(), 
            request.platform()
        );
        return ResponseEntity.ok(connection);
    }
    
    @PostMapping("/disconnect")
    public ResponseEntity<Map<String, String>> disconnectPlatform(@RequestBody ConnectRequest request) {
        socialMediaService.disconnectPlatform(request.businessId(), request.platform());
        return ResponseEntity.ok(Map.of("message", "Disconnected successfully"));
    }
    
    @GetMapping("/connections/{businessId}")
    public ResponseEntity<List<SocialConnection>> getConnections(@PathVariable Long businessId) {
        return ResponseEntity.ok(socialMediaService.getConnections(businessId));
    }
    
    record ConnectRequest(Long businessId, Post.Platform platform) {}
}
