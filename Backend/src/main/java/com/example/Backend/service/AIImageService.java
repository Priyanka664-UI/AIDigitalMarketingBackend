package com.example.Backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Service
public class AIImageService {
    
    private final WebClient webClient;
    
    public AIImageService() {
        this.webClient = WebClient.builder().build();
    }
    
    public String generateImage(String prompt) {
        try {
            // Use Pollinations.ai - free, no auth, works reliably
            String encodedPrompt = java.net.URLEncoder.encode(prompt, "UTF-8");
            byte[] imageBytes = webClient.get()
                .uri("https://image.pollinations.ai/prompt/" + encodedPrompt + "?width=1024&height=1024&nologo=true")
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
            
            if (imageBytes != null && imageBytes.length > 100) {
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (Exception e) {
            System.err.println("Image API failed: " + e.getMessage());
        }
        
        return generateSVGFallback(prompt);
    }
    
    private String generateSVGFallback(String prompt) {
        int hash = Math.abs(prompt.hashCode());
        int hue1 = hash % 360;
        int hue2 = (hue1 + 120) % 360;
        
        String svg = String.format(
            "<svg xmlns='http://www.w3.org/2000/svg' width='1024' height='1024'>" +
            "<defs><linearGradient id='g' x1='0%%' y1='0%%' x2='100%%' y2='100%%'>" +
            "<stop offset='0%%' style='stop-color:hsl(%d,70%%,50%%)'/>" +
            "<stop offset='100%%' style='stop-color:hsl(%d,70%%,50%%)'/>" +
            "</linearGradient></defs>" +
            "<rect width='1024' height='1024' fill='url(#g)'/>" +
            "<text x='50%%' y='45%%' font-family='Arial' font-size='48' font-weight='bold' " +
            "fill='white' text-anchor='middle' opacity='0.9'>%s</text>" +
            "<text x='50%%' y='55%%' font-family='Arial' font-size='24' " +
            "fill='white' text-anchor='middle' opacity='0.7'>AI Generated</text>" +
            "</svg>",
            hue1, hue2, prompt.length() > 30 ? prompt.substring(0, 30) : prompt
        );
        
        return "data:image/svg+xml;base64," + Base64.getEncoder().encodeToString(svg.getBytes());
    }
}
