package com.example.Backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
public class AIImageService {
    
    @Value("${huggingface.api.key}")
    private String apiKey;
    
    @Value("${huggingface.api.url}")
    private String apiUrl;
    
    private final WebClient webClient;
    
    public AIImageService() {
        this.webClient = WebClient.builder()
            .codecs(c -> c.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
            .build();
    }
    
    public String generateImage(String prompt) {
        log.info("Generating image for prompt: {}", prompt);
        
        try {
            String enhancedPrompt = enhancePromptForMarketing(prompt);
            log.info("Enhanced prompt: {}", enhancedPrompt);
            
            Map<String, Object> body = Map.of(
                "inputs", enhancedPrompt
            );
            
            log.info("Calling HuggingFace API at: {}", apiUrl);
            
            byte[] imageBytes = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(byte[].class)
                .timeout(Duration.ofSeconds(60))
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("HuggingFace API error - Status: {}, Body: {}", 
                        e.getStatusCode(), e.getResponseBodyAsString());
                })
                .doOnError(e -> {
                    log.error("Error calling HuggingFace API: {}", e.getMessage(), e);
                })
                .onErrorResume(e -> {
                    log.warn("Falling back to SVG due to error: {}", e.getMessage());
                    return Mono.empty();
                })
                .block();
            
            if (imageBytes != null && imageBytes.length > 0) {
                log.info("Successfully generated image, size: {} bytes", imageBytes.length);
                return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
            } else {
                log.warn("No image bytes received from API");
            }
        } catch (Exception e) {
            log.error("Image generation failed with exception: {}", e.getMessage(), e);
        }
        
        log.info("Using SVG fallback");
        return generateSVGFallback(prompt);
    }
    
    private String enhancePromptForMarketing(String prompt) {
        return "Professional digital marketing advertisement image, " + prompt +
               ", high quality, modern design, vibrant colors, " +
               "brand identity, commercial photography style, " +
               "clean background, 4K, sharp focus";
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
