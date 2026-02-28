package com.example.Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("name", "John Doe");
        user.put("email", "john@example.com");
        user.put("phone", "+1 234 567 8900");
        user.put("role", "User");
        user.put("createdAt", "2024-01-15");
        user.put("lastLogin", "2024-01-20 10:30 AM");
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User updated successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePasswordPost(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/upgrade-plan")
    public ResponseEntity<?> upgradePlan(@PathVariable Long id, @RequestBody Map<String, String> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Upgraded to " + data.get("plan") + " plan successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/ai-preferences")
    public ResponseEntity<?> getAIPreferences(@PathVariable Long id) {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("defaultTone", "Professional");
        preferences.put("defaultImageStyle", "Realistic");
        preferences.put("contentFrequency", "Weekly");
        preferences.put("autoSchedule", false);
        preferences.put("aiSuggestions", true);
        return ResponseEntity.ok(preferences);
    }

    @PostMapping("/{id}/ai-preferences")
    public ResponseEntity<?> saveAIPreferences(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "AI preferences saved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/notification-settings")
    public ResponseEntity<?> getNotificationSettings(@PathVariable Long id) {
        Map<String, Object> settings = new HashMap<>();
        settings.put("emailPost", true);
        settings.put("emailFail", true);
        settings.put("emailWeekly", true);
        settings.put("emailTips", false);
        settings.put("pushPost", true);
        settings.put("pushEngagement", false);
        return ResponseEntity.ok(settings);
    }

    @PostMapping("/{id}/notification-settings")
    public ResponseEntity<?> saveNotificationSettings(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification settings saved successfully");
        return ResponseEntity.ok(response);
    }
}
