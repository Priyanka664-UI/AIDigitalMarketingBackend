package com.example.Backend.controller;

import com.example.Backend.model.Post;
import com.example.Backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/content")
@CrossOrigin(origins = "*")
public class ContentController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/calendar")
    public ResponseEntity<?> getCalendar(@RequestParam Long businessId) {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/library")
    public ResponseEntity<?> getLibrary(@RequestParam Long businessId) {
        Map<String, Object> library = new HashMap<>();
        library.put("savedPosts", postRepository.findAll());
        library.put("aiGeneratedPosts", new ArrayList<>());
        library.put("uploadedMedia", new ArrayList<>());
        library.put("hashtagCollections", new ArrayList<>());
        return ResponseEntity.ok(library);
    }

    @GetMapping("/drafts")
    public ResponseEntity<?> getDrafts(@RequestParam Long businessId) {
        List<Post> drafts = postRepository.findAll().stream()
            .filter(p -> p.getStatus() == Post.PostStatus.SCHEDULED)
            .toList();
        return ResponseEntity.ok(drafts);
    }

    @PutMapping("/drafts/{id}")
    public ResponseEntity<?> updateDraft(@PathVariable Long id, @RequestBody Post post) {
        Post existing = postRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setCaption(post.getCaption());
            existing.setHashtags(post.getHashtags());
            postRepository.save(existing);
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.notFound().build();
    }
}
