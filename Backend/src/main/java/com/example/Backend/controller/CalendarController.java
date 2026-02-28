package com.example.Backend.controller;

import com.example.Backend.model.Post;
import com.example.Backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "*")
public class CalendarController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts")
    public ResponseEntity<?> getCalendarPosts(@RequestParam Long businessId, @RequestParam int year, @RequestParam int month) {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getCalendarSummary(@RequestParam Long businessId) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalPosts", postRepository.count());
        summary.put("scheduledPosts", postRepository.findAll().stream().filter(p -> p.getStatus() == Post.PostStatus.SCHEDULED).count());
        summary.put("publishedPosts", postRepository.findAll().stream().filter(p -> p.getStatus() == Post.PostStatus.PUBLISHED).count());
        return ResponseEntity.ok(summary);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Post deleted"));
    }

    @GetMapping("/posts/platform/{platform}")
    public ResponseEntity<?> getPostsByPlatform(@PathVariable String platform, @RequestParam Long businessId) {
        List<Post> posts = postRepository.findAll().stream()
            .filter(p -> p.getPlatform().name().equals(platform.toUpperCase()))
            .toList();
        return ResponseEntity.ok(posts);
    }
}
