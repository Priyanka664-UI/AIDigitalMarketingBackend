package com.example.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class SocialConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long businessId;
    
    @Enumerated(EnumType.STRING)
    private Post.Platform platform;
    
    private String accessToken;
    private Boolean isConnected = false;
    private LocalDateTime connectedAt;
    private LocalDateTime expiresAt;
}
