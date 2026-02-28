package com.example.Backend.repository;

import com.example.Backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCampaignId(Long campaignId);
<<<<<<< HEAD
    List<Post> findByCampaignIdAndStatus(Long campaignId, Post.PostStatus status);
    List<Post> findByStatusAndScheduledTimeBefore(Post.PostStatus status, LocalDateTime time);
=======

    List<Post> findByCampaignBusinessId(Long businessId);
>>>>>>> 9e243e6b7d4225a3849ed0b432b7a3845f9cb1c9
}
