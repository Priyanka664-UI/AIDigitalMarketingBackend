package com.example.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "business")
@Data
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name")
    private String businessName;
    
    @Column(name = "product_details")
    private String productDetails;
    
    @Column(name = "target_audience")
    private String targetAudience;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand_tone")
    private BrandTone brandTone;

    @Enumerated(EnumType.STRING)
    @Column(name = "marketing_goal")
    private MarketingGoal marketingGoal;

    @Column(name = "industry")
    private String industry;
    
    @Column(name = "ai_credits")
    private Integer aiCredits = 100;
    
    @Column(name = "credits_reset_days")
    private Integer creditsResetDays = 15;
    
    @Column(name = "tier")
    private String tier = "Pro Plan";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum BrandTone {
        PROFESSIONAL, CASUAL, PREMIUM, FRIENDLY, FORMAL
    }

    public enum MarketingGoal {
        LEADS, SALES, AWARENESS, ENGAGEMENT
    }
}
