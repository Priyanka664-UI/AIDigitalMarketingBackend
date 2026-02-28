package com.example.Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    @GetMapping("/ai-preferences/{userId}")
    public ResponseEntity<?> getAIPreferences(@PathVariable Long userId) {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("defaultTone", "Professional");
        preferences.put("defaultPlatform", "Instagram");
        preferences.put("autoGenerateImages", true);
        preferences.put("aiSuggestions", true);
        return ResponseEntity.ok(preferences);
    }

    @PostMapping("/ai-preferences/{userId}")
    public ResponseEntity<?> saveAIPreferences(@PathVariable Long userId, @RequestBody Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "AI preferences saved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<?> getNotifications(@PathVariable Long userId) {
        Map<String, Object> notifications = new HashMap<>();
        notifications.put("emailNotifications", true);
        notifications.put("postReminders", true);
        notifications.put("campaignUpdates", false);
        notifications.put("weeklyReports", true);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/notifications/{userId}")
    public ResponseEntity<?> saveNotifications(@PathVariable Long userId, @RequestBody Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification preferences saved successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/platforms/connect")
    public ResponseEntity<?> connectPlatform(@RequestBody Map<String, Object> data) {
        String platform = (String) data.get("platform");
        Long userId = Long.valueOf(data.get("userId").toString());
        Map<String, String> response = new HashMap<>();
        response.put("message", platform + " connected successfully");
        response.put("status", "connected");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/platforms/disconnect")
    public ResponseEntity<?> disconnectPlatform(@RequestBody Map<String, Object> data) {
        String platform = (String) data.get("platform");
        Map<String, String> response = new HashMap<>();
        response.put("message", platform + " disconnected successfully");
        response.put("status", "disconnected");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subscription/{userId}")
    public ResponseEntity<?> getSubscription(@PathVariable Long userId) {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("plan", "Free");
        subscription.put("status", "Active");
        subscription.put("renewalDate", "N/A");
        subscription.put("postsLimit", 10);
        subscription.put("postsUsed", 3);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/subscription/upgrade")
    public ResponseEntity<?> upgradePlan(@RequestBody Map<String, Object> data) {
        String plan = (String) data.get("plan");
        Long userId = Long.valueOf(data.get("userId").toString());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Upgraded to " + plan + " plan successfully");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/scheduling/{userId}")
    public ResponseEntity<?> getSchedulingPreferences(@PathVariable Long userId) {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("defaultPostingTime", "10:00");
        preferences.put("timeZone", "UTC");
        preferences.put("autoSchedule", false);
        return ResponseEntity.ok(preferences);
    }

    @PostMapping("/scheduling/{userId}")
    public ResponseEntity<?> saveSchedulingPreferences(@PathVariable Long userId, @RequestBody Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Scheduling preferences saved successfully");
        return ResponseEntity.ok(response);
    }
}
