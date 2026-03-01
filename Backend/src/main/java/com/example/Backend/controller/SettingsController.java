package com.example.Backend.controller;

import com.example.Backend.model.Settings;
import com.example.Backend.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    // CREATE/READ - Get or create settings for user
    @GetMapping("/{userId}")
    public ResponseEntity<Settings> getSettings(@PathVariable Long userId) {
        return ResponseEntity.ok(settingsService.getOrCreateSettings(userId));
    }

    // UPDATE - Update all settings
    @PutMapping("/{userId}")
    public ResponseEntity<Settings> updateSettings(@PathVariable Long userId, @RequestBody Settings settings) {
        return ResponseEntity.ok(settingsService.updateSettings(userId, settings));
    }

    // DELETE - Delete settings
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteSettings(@PathVariable Long userId) {
        settingsService.deleteSettings(userId);
        return ResponseEntity.ok(Map.of("message", "Settings deleted successfully"));
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/ai-preferences/{userId}")
    public ResponseEntity<Settings> getAIPreferences(@PathVariable Long userId) {
        return ResponseEntity.ok(settingsService.getOrCreateSettings(userId));
    }

    @PostMapping("/ai-preferences/{userId}")
    public ResponseEntity<Map<String, String>> saveAIPreferences(@PathVariable Long userId, @RequestBody Settings settings) {
        settingsService.updateSettings(userId, settings);
        return ResponseEntity.ok(Map.of("message", "AI preferences saved successfully"));
    }

    @GetMapping("/notifications/{userId}")
    public ResponseEntity<Settings> getNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(settingsService.getOrCreateSettings(userId));
    }

    @PostMapping("/notifications/{userId}")
    public ResponseEntity<Map<String, String>> saveNotifications(@PathVariable Long userId, @RequestBody Settings settings) {
        settingsService.updateSettings(userId, settings);
        return ResponseEntity.ok(Map.of("message", "Notification preferences saved successfully"));
    }

    @PostMapping("/platforms/connect")
    public ResponseEntity<Map<String, String>> connectPlatform(@RequestBody Map<String, Object> data) {
        String platform = (String) data.get("platform");
        return ResponseEntity.ok(Map.of("message", platform + " connected successfully", "status", "connected"));
    }

    @PostMapping("/platforms/disconnect")
    public ResponseEntity<Map<String, String>> disconnectPlatform(@RequestBody Map<String, Object> data) {
        String platform = (String) data.get("platform");
        return ResponseEntity.ok(Map.of("message", platform + " disconnected successfully", "status", "disconnected"));
    }

    @GetMapping("/subscription/{userId}")
    public ResponseEntity<Map<String, Object>> getSubscription(@PathVariable Long userId) {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("plan", "Free");
        subscription.put("status", "Active");
        subscription.put("renewalDate", "N/A");
        subscription.put("postsLimit", 10);
        subscription.put("postsUsed", 3);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/subscription/upgrade")
    public ResponseEntity<Map<String, String>> upgradePlan(@RequestBody Map<String, Object> data) {
        String plan = (String) data.get("plan");
        return ResponseEntity.ok(Map.of("message", "Upgraded to " + plan + " plan successfully", "status", "success"));
    }

    @GetMapping("/scheduling/{userId}")
    public ResponseEntity<Settings> getSchedulingPreferences(@PathVariable Long userId) {
        return ResponseEntity.ok(settingsService.getOrCreateSettings(userId));
    }

    @PostMapping("/scheduling/{userId}")
    public ResponseEntity<Map<String, String>> saveSchedulingPreferences(@PathVariable Long userId, @RequestBody Settings settings) {
        settingsService.updateSettings(userId, settings);
        return ResponseEntity.ok(Map.of("message", "Scheduling preferences saved successfully"));
    }
}
