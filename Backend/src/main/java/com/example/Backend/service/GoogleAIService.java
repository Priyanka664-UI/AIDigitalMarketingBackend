package com.example.Backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class GoogleAIService {

    @Value("${google.ai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateImage(String prompt) {
        System.out.println("Generating image with Google AI for: " + prompt);
        
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/imagen-3.0-generate-001:predict?key=" + apiKey;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> instances = new HashMap<>();
            instances.put("prompt", prompt);
            requestBody.put("instances", Arrays.asList(instances));
            requestBody.put("parameters", Map.of(
                "sampleCount", 1,
                "aspectRatio", "1:1",
                "safetyFilterLevel", "block_some",
                "personGeneration", "allow_adult"
            ));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            System.out.println("Calling Google Imagen API...");
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                System.out.println("Response: " + response.getBody());
                List<Map<String, Object>> predictions = (List<Map<String, Object>>) response.getBody().get("predictions");
                if (predictions != null && !predictions.isEmpty()) {
                    String imageData = (String) predictions.get(0).get("bytesBase64Encoded");
                    if (imageData != null) {
                        System.out.println("Image generated successfully");
                        return "data:image/png;base64," + imageData;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Google AI Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Falling back to placeholder");
        return generatePlaceholderImage(prompt);
    }

    private String generatePlaceholderImage(String prompt) {
        String shortPrompt = prompt.length() > 50 ? prompt.substring(0, 50) + "..." : prompt;
        String encoded = java.net.URLEncoder.encode(shortPrompt, java.nio.charset.StandardCharsets.UTF_8);
        
        return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='1024' height='1024'%3E" +
               "%3Cdefs%3E%3ClinearGradient id='grad' x1='0%25' y1='0%25' x2='100%25' y2='100%25'%3E" +
               "%3Cstop offset='0%25' style='stop-color:%23667eea;stop-opacity:1'/%3E" +
               "%3Cstop offset='100%25' style='stop-color:%23764ba2;stop-opacity:1'/%3E" +
               "%3C/linearGradient%3E%3C/defs%3E" +
               "%3Crect fill='url(%23grad)' width='1024' height='1024'/%3E" +
               "%3Ctext x='50%25' y='45%25' dominant-baseline='middle' text-anchor='middle' " +
               "font-family='Arial,sans-serif' font-size='48' font-weight='bold' fill='white'%3E" +
               "%F0%9F%8E%A8 AI Generated%3C/text%3E" +
               "%3Ctext x='50%25' y='55%25' dominant-baseline='middle' text-anchor='middle' " +
               "font-family='Arial,sans-serif' font-size='20' fill='white' opacity='0.9'%3E" +
               encoded + "%3C/text%3E%3C/svg%3E";
    }
}
