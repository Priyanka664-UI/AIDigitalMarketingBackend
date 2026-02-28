package com.example.Backend.service;

import com.example.Backend.model.Business;
import com.example.Backend.model.Post;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AIContentService {
    
    public String generateCaption(Business business, Post.Platform platform) {
        String tone = getToneStyle(business.getBrandTone());
        String goal = getGoalCTA(business.getMarketingGoal());
        
        List<String> templates = getCaptionTemplates(platform, business.getBrandTone());
        String template = templates.get(new Random().nextInt(templates.size()));
        
        return String.format(template, 
            business.getProductDetails(), 
            business.getTargetAudience(),
            goal);
    }
    
    public String generateHashtags(Business business, Post.Platform platform) {
        List<String> hashtags = new ArrayList<>();
        
        // Industry-specific hashtags
        if (business.getIndustry() != null) {
            hashtags.add("#" + business.getIndustry().replaceAll("\\s+", ""));
        }
        
        // Goal-based hashtags
        switch (business.getMarketingGoal()) {
            case SALES -> hashtags.addAll(List.of("#Sale", "#ShopNow", "#BuyNow"));
            case AWARENESS -> hashtags.addAll(List.of("#BrandAwareness", "#Discover", "#NewBrand"));
            case LEADS -> hashtags.addAll(List.of("#LearnMore", "#GetStarted", "#JoinUs"));
            case ENGAGEMENT -> hashtags.addAll(List.of("#Community", "#Engage", "#Connect"));
        }
        
        // Platform-specific hashtags
        if (platform == Post.Platform.INSTAGRAM) {
            hashtags.addAll(List.of("#InstaGood", "#Trending"));
        } else if (platform == Post.Platform.LINKEDIN) {
            hashtags.addAll(List.of("#Business", "#Professional"));
        }
        
        return String.join(" ", hashtags);
    }
    
    private List<String> getCaptionTemplates(Post.Platform platform, Business.BrandTone tone) {
        if (tone == Business.BrandTone.FORMAL || tone == Business.BrandTone.PROFESSIONAL) {
            return List.of(
                "Introducing %s - designed for %s. %s",
                "We're proud to present %s, tailored specifically for %s. %s",
                "Discover %s - the perfect solution for %s. %s"
            );
        } else if (tone == Business.BrandTone.FRIENDLY || tone == Business.BrandTone.CASUAL) {
            return List.of(
                "Hey %s! 👋 Check out %s - made just for you! %s",
                "Exciting news! %s is here for %s! %s 🎉",
                "You're gonna love this! %s for %s. %s ✨"
            );
        } else {
            return List.of(
                "Experience luxury with %s - exclusively for %s. %s",
                "Elevate your lifestyle with %s, crafted for %s. %s",
                "Premium %s designed for discerning %s. %s"
            );
        }
    }
    
    private String getToneStyle(Business.BrandTone tone) {
        return switch (tone) {
            case FORMAL -> "professional and respectful";
            case FRIENDLY -> "warm and approachable";
            case PREMIUM -> "sophisticated and exclusive";
            case CASUAL -> "relaxed and conversational";
            case PROFESSIONAL -> "expert and authoritative";
        };
    }
    
    private String getGoalCTA(Business.MarketingGoal goal) {
        return switch (goal) {
            case SALES -> "Shop now and get 20% off!";
            case AWARENESS -> "Follow us to learn more!";
            case LEADS -> "Sign up today for exclusive updates!";
            case ENGAGEMENT -> "Share your thoughts in the comments!";
        };
    }
    
    public String generateImagePrompt(Business business) {
        return String.format("Professional marketing image for %s targeting %s, %s style",
            business.getProductDetails(),
            business.getTargetAudience(),
            business.getBrandTone().toString().toLowerCase());
    }
}
