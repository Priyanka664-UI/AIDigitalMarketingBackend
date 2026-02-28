package com.example.Backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAIService {

    @Value("${openai.api.key:your-openai-api-key-here}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateImage(String prompt) {
        System.out.println("OpenAI Service - Generating image");
        System.out.println("API Key configured: " + (apiKey != null && !apiKey.isEmpty() && !apiKey.equals("your-openai-api-key-here") && !apiKey.startsWith("sk-proj-YOUR")));
        
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-openai-api-key-here") || apiKey.startsWith("sk-proj-YOUR")) {
            System.out.println("Using Unsplash fallback - API key not configured");
            // Use Unsplash for free images based on prompt
            String searchTerm = prompt.replace(" ", ",");
            return "https://source.unsplash.com/1024x1024/?" + searchTerm;
        }
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "dall-e-3");
            requestBody.put("prompt", prompt);
            requestBody.put("n", 1);
            requestBody.put("size", "1024x1024");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            System.out.println("Calling OpenAI API...");
            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/images/generations",
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, String>> data = (List<Map<String, String>>) response.getBody().get("data");
                if (data != null && !data.isEmpty()) {
                    String imageUrl = data.get(0).get("url");
                    System.out.println("Success: " + imageUrl);
                    return imageUrl;
                }
            }
        } catch (Exception e) {
            System.err.println("OpenAI Error: " + e.getMessage());
        }
        
        String searchTerm = prompt.replace(" ", ",");
        return "https://source.unsplash.com/1024x1024/?" + searchTerm;
    }

    public String generateCaption(String businessName, String tone, String topic) {
        try {
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-openai-api-key-here")) {
                System.out.println("Using fallback caption - API key not configured");
                return String.format("Check out our latest %s! Perfect for %s. #%s #marketing #socialmedia",
                    topic, tone.toLowerCase() + " audience", businessName.toLowerCase().replace(" ", ""));
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            String systemPrompt = "You are a social media marketing expert. Generate engaging captions.";
            String userPrompt = String.format(
                "Generate a %s tone social media caption for %s about %s. Include relevant hashtags.",
                tone, businessName, topic
            );

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4");
            requestBody.put("messages", Arrays.asList(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
            ));
            requestBody.put("max_tokens", 200);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl + "/chat/completions",
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, String> message = (Map<String, String>) choices.get(0).get("message");
                    return message.get("content");
                }
            }
        } catch (Exception e) {
            System.err.println("Error generating caption: " + e.getMessage());
        }
        
        return String.format("Exciting update from %s! Stay tuned for more. #%s #marketing #socialmedia",
            businessName, businessName.toLowerCase().replace(" ", ""));
    }
}
