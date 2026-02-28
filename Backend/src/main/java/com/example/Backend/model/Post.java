package com.example.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;
    
    @Enumerated(EnumType.STRING)
    private Platform platform;
    
    @Column(length = 2000)
    private String caption;
    
    private String hashtags;
    private String imageUrl;
    private LocalDateTime scheduledTime;
    
    @Enumerated(EnumType.STRING)
    private PostStatus status;
    
    private Integer likes = 0;
    private Integer shares = 0;
    private Integer comments = 0;
    private Integer reach = 0;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum Platform {
        INSTAGRAM, FACEBOOK, LINKEDIN, WHATSAPP
    }
    
    public enum PostStatus {
        SCHEDULED, PUBLISHED, FAILED
    }
}
