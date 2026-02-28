package com.example.Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/platforms")
@CrossOrigin(origins = "*")
public class PlatformController {

    @PostMapping("/connect")
    public ResponseEntity<?> connectPlatform(@RequestBody Map<String, Object> data) {
        String platform = (String) data.get("platform");
        Map<String, String> response = new HashMap<>();
        response.put("message", platform + " connected successfully");
        response.put("status", "connected");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/disconnect")
    public ResponseEntity<?> disconnectPlatform(@RequestBody Map<String, Object> data) {
        String platform = (String) data.get("platform");
        Map<String, String> response = new HashMap<>();
        response.put("message", platform + " disconnected successfully");
        response.put("status", "disconnected");
        return ResponseEntity.ok(response);
    }
}
