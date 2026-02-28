package com.example.Backend.dto;

import lombok.Data;

@Data
public class CreativeGenerationRequest {
    private String designType; // POSTER, CAROUSEL, AD_BANNER
    private String productName;
    private String tagline;
    private String colorScheme;
    private String style; // MODERN, MINIMAL, VIBRANT, ELEGANT
}
