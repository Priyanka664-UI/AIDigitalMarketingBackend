package com.example.Backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MarketingStrategyRequest {
    private String businessName;
    private String businessCategory;
    private String targetAudience;
    private String primaryGoal;
    private String budgetLevel;
    private List<String> platforms;
    
    // Extended fields for detailed strategy
    private String category;
    private String description;
    private String ageRange;
    private String pain;
    private String goal;
    private String budget;
    private String geo;
}
