package com.example.Backend.dto;

import lombok.Data;

@Data
public class ContentGenerationRequest {
    private String productName;
    private String targetAudience;
    private String tone; // PROFESSIONAL, FRIENDLY, FUNNY
    private String goal; // SALES, AWARENESS, ENGAGEMENT
    private String platform; // INSTAGRAM, FACEBOOK, LINKEDIN, WHATSAPP
}
