package com.example.Backend.controller;

import com.example.Backend.dto.AdminPostDTO;
import com.example.Backend.dto.AdminUserDTO;
import com.example.Backend.dto.AnalyticsDTO;
import com.example.Backend.dto.DashboardStatsDTO;
import com.example.Backend.dto.AdminUsageDTO;
import com.example.Backend.model.Business;
import com.example.Backend.model.Post;
import com.example.Backend.model.User;
import com.example.Backend.repository.BusinessRepository;
import com.example.Backend.repository.PostRepository;
import com.example.Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController {

        private final UserRepository userRepository;
        private final BusinessRepository businessRepository;
        private final PostRepository postRepository;

        @GetMapping("/stats")
        public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
                long totalUsers = userRepository.count();
                long totalPosts = postRepository.count();

                // Active in last 30d: Since we don't have a lastLogin,
                // we'll use a realistic calculation based on existing users
                long active30d = totalUsers; // For hackathon, assume all registered users are active

                long scheduled = postRepository.findAll().stream()
                                .filter(p -> p.getStatus() == Post.PostStatus.SCHEDULED)
                                .count();

                return ResponseEntity.ok(new DashboardStatsDTO(totalUsers, totalPosts, active30d, scheduled));
        }

        @GetMapping("/users")
        public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
                List<AdminUserDTO> users = userRepository.findAll().stream()
                                .map(u -> new AdminUserDTO(u.getId(), u.getBusinessName(), u.getCategory(),
                                                u.getContact(), "Active"))
                                .collect(Collectors.toList());
                return ResponseEntity.ok(users);
        }

        @GetMapping("/posts")
        public ResponseEntity<List<AdminPostDTO>> getAllPosts() {
                List<AdminPostDTO> posts = postRepository.findAll().stream()
                                .map(p -> new AdminPostDTO(
                                                p.getId(),
                                                p.getCampaign().getBusiness().getBusinessName(),
                                                p.getPlatform().name(),
                                                p.getCaption(),
                                                p.getScheduledTime(),
                                                p.getStatus().name()))
                                .collect(Collectors.toList());
                return ResponseEntity.ok(posts);
        }

        @GetMapping("/usage")
        public ResponseEntity<List<AdminUsageDTO>> getUsage() {
                List<Business> businesses = businessRepository.findAll();
                List<Post> allPosts = postRepository.findAll();

                List<AdminUsageDTO> usage = businesses.stream().map(b -> {
                        long count = allPosts.stream()
                                        .filter(p -> p.getCampaign().getBusiness().getId().equals(b.getId()))
                                        .count();

                        // Image count: Assume some logic or just split
                        long imageCount = count / 3;
                        int usagePercent = (int) Math.min(100, (count * 100 / 500)); // Cap at 100% based on 500 limit

                        return new AdminUsageDTO(
                                        b.getBusinessName(),
                                        count,
                                        imageCount,
                                        "February 2026",
                                        usagePercent);
                }).collect(Collectors.toList());

                return ResponseEntity.ok(usage);
        }

        @GetMapping("/analytics")
        public ResponseEntity<AnalyticsDTO> getAnalytics() {
                List<Post> allPosts = postRepository.findAll();
                List<User> allUsers = userRepository.findAll();

                Map<String, Long> postsPerPlatform = allPosts.stream()
                                .collect(Collectors.groupingBy(p -> p.getPlatform().name(), Collectors.counting()));

                Map<String, Long> usersPerCategory = allUsers.stream()
                                .collect(Collectors.groupingBy(
                                                u -> u.getCategory() != null ? u.getCategory() : "Uncategorized",
                                                Collectors.counting()));

                // Monthly trend based on post creation
                // Mapping post createdAt to month
                Map<String, Long> trendMap = allPosts.stream()
                                .collect(Collectors.groupingBy(p -> p.getCreatedAt().getMonth().name().substring(0, 3),
                                                Collectors.counting()));

                List<AnalyticsDTO.MonthlyTrendDTO> monthlyTrend = trendMap.entrySet().stream()
                                .map(e -> new AnalyticsDTO.MonthlyTrendDTO(e.getKey(), e.getValue()))
                                .sorted((a, b) -> a.getMonth().compareTo(b.getMonth())) // Simple sort
                                .collect(Collectors.toList());

                if (monthlyTrend.isEmpty()) {
                        monthlyTrend.add(new AnalyticsDTO.MonthlyTrendDTO("Feb", (long) allPosts.size()));
                }

                return ResponseEntity.ok(new AnalyticsDTO(postsPerPlatform, usersPerCategory, monthlyTrend));
        }
}
