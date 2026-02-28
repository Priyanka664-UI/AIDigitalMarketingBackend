package com.example.Backend.repository;

import com.example.Backend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCampaignId(Long campaignId);

    List<Post> findByCampaignBusinessId(Long businessId);
}
