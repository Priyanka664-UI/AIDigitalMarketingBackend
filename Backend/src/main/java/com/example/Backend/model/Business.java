package com.example.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String businessName;
    private String productDetails;
    private String targetAudience;
    
    @Enumerated(EnumType.STRING)
    private BrandTone brandTone;
    
    @Enumerated(EnumType.STRING)
    private MarketingGoal marketingGoal;
    
    private String industry;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum BrandTone {
        FORMAL, FRIENDLY, PREMIUM, CASUAL, PROFESSIONAL
    }
    
    public enum MarketingGoal {
        SALES, AWARENESS, LEADS, ENGAGEMENT
    }
}
