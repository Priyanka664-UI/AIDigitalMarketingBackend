package com.example.Backend.service;

import com.example.Backend.dto.*;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.util.*;

@Service
public class ContentManagementService {
    
    public ContentGenerationResponse generateContent(ContentGenerationRequest request) {
        List<String> captions = generateCaptions(request);
        List<String> hashtags = generateHashtags(request);
        List<String> postIdeas = generatePostIdeas(request);
        List<ContentGenerationResponse.ContentCalendarItem> calendar = generateContentCalendar(request);
        
        return new ContentGenerationResponse(captions, hashtags, postIdeas, calendar);
    }
    
    private List<String> generateCaptions(ContentGenerationRequest request) {
        List<String> captions = new ArrayList<>();
        String cta = getCallToAction(request.getGoal());
        
        if ("PROFESSIONAL".equalsIgnoreCase(request.getTone())) {
            captions.add(String.format("Introducing %s - the professional solution for %s. %s", 
                request.getProductName(), request.getTargetAudience(), cta));
            captions.add(String.format("Elevate your experience with %s, designed specifically for %s. %s", 
                request.getProductName(), request.getTargetAudience(), cta));
            captions.add(String.format("Discover %s - where quality meets innovation for %s. %s", 
                request.getProductName(), request.getTargetAudience(), cta));
        } else if ("FRIENDLY".equalsIgnoreCase(request.getTone())) {
            captions.add(String.format("Hey there! 👋 Meet %s - your new favorite for %s! %s", 
                request.getProductName(), request.getTargetAudience(), cta));
            captions.add(String.format("Exciting news! %s is here to make life easier for %s! %s 🎉", 
                request.getProductName(), request.getTargetAudience(), cta));
            captions.add(String.format("You're going to love %s! Perfect for %s. %s ✨", 
                request.getProductName(), request.getTargetAudience(), cta));
        } else { // FUNNY
            captions.add(String.format("Plot twist: %s just made %s way cooler! 😎 %s", 
                request.getProductName(), request.getTargetAudience(), cta));
            captions.add(String.format("Warning: %s may cause extreme satisfaction for %s! 🚀 %s", 
                request.getProductName(), request.getTargetAudience(), cta));
            captions.add(String.format("Breaking news: %s is officially the best thing for %s! 🔥 %s", 
                request.getProductName(), request.getTargetAudience(), cta));
        }
        
        return captions;
    }
    
    private List<String> generateHashtags(ContentGenerationRequest request) {
        List<String> hashtags = new ArrayList<>();
        
        // Goal-based hashtags
        if ("SALES".equalsIgnoreCase(request.getGoal())) {
            hashtags.addAll(List.of("#Sale", "#ShopNow", "#BuyNow", "#Discount", "#Offer"));
        } else if ("AWARENESS".equalsIgnoreCase(request.getGoal())) {
            hashtags.addAll(List.of("#BrandAwareness", "#Discover", "#NewBrand", "#Innovation", "#Trending"));
        } else { // ENGAGEMENT
            hashtags.addAll(List.of("#Community", "#Engage", "#Connect", "#ShareYourStory", "#JoinUs"));
        }
        
        // Platform-specific hashtags
        if ("INSTAGRAM".equalsIgnoreCase(request.getPlatform())) {
            hashtags.addAll(List.of("#InstaGood", "#InstaDaily", "#Viral"));
        } else if ("LINKEDIN".equalsIgnoreCase(request.getPlatform())) {
            hashtags.addAll(List.of("#Business", "#Professional", "#Leadership"));
        } else if ("FACEBOOK".equalsIgnoreCase(request.getPlatform())) {
            hashtags.addAll(List.of("#SocialMedia", "#Community", "#Connect"));
        }
        
        return hashtags;
    }
    
    private List<String> generatePostIdeas(ContentGenerationRequest request) {
        return List.of(
            "Product showcase with customer testimonials",
            "Behind-the-scenes of " + request.getProductName(),
            "User-generated content featuring " + request.getProductName(),
            "Educational post about benefits for " + request.getTargetAudience(),
            "Interactive poll or quiz related to " + request.getProductName(),
            "Before/After transformation story",
            "Limited-time offer announcement",
            "Tips and tricks for " + request.getTargetAudience()
        );
    }
    
    private List<ContentGenerationResponse.ContentCalendarItem> generateContentCalendar(ContentGenerationRequest request) {
        List<ContentGenerationResponse.ContentCalendarItem> calendar = new ArrayList<>();
        String[] days = {"Monday", "Wednesday", "Friday"};
        String[] postTypes = {"Product Feature", "Customer Story", "Engagement Post"};
        
        for (int i = 0; i < 3; i++) {
            String caption = generateCaptions(request).get(i);
            String hashtags = String.join(" ", generateHashtags(request).subList(0, 5));
            calendar.add(new ContentGenerationResponse.ContentCalendarItem(
                days[i], postTypes[i], caption, hashtags
            ));
        }
        
        return calendar;
    }
    
    private String getCallToAction(String goal) {
        return switch (goal.toUpperCase()) {
            case "SALES" -> "Shop now and save 20%! 🛒";
            case "AWARENESS" -> "Follow us to learn more! 👉";
            case "ENGAGEMENT" -> "Share your thoughts below! 💬";
            default -> "Learn more today!";
        };
    }
    
    public Map<String, String> generateCreative(CreativeGenerationRequest request) {
        Map<String, String> creative = new HashMap<>();
        
        String prompt = String.format(
            "Create a %s %s design for '%s' with tagline '%s' in %s color scheme, %s style",
            request.getStyle(), request.getDesignType(), request.getProductName(),
            request.getTagline(), request.getColorScheme(), request.getStyle()
        );
        
        creative.put("designType", request.getDesignType());
        creative.put("aiPrompt", prompt);
        creative.put("mockImageUrl", "https://via.placeholder.com/1080x1080?text=" + 
            request.getProductName().replaceAll(" ", "+"));
        creative.put("canvaTemplateUrl", "https://www.canva.com/templates/");
        creative.put("dimensions", getDimensions(request.getDesignType()));
        
        return creative;
    }
    
    private String getDimensions(String designType) {
        return switch (designType.toUpperCase()) {
            case "POSTER" -> "1080x1350";
            case "CAROUSEL" -> "1080x1080";
            case "AD_BANNER" -> "1200x628";
            default -> "1080x1080";
        };
    }
}
