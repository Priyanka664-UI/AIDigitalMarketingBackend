package com.example.Backend.repository;

import com.example.Backend.model.Post;
import com.example.Backend.model.SocialConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SocialConnectionRepository extends JpaRepository<SocialConnection, Long> {
    List<SocialConnection> findByBusinessId(Long businessId);
    Optional<SocialConnection> findByBusinessIdAndPlatform(Long businessId, Post.Platform platform);
}
