package com.example.Backend.service;

import com.example.Backend.dto.MarketingStrategyRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AnthropicService {

    @Value("${anthropic.api.key:}")
    private String apiKey;

    @Value("${anthropic.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String SYSTEM_PROMPT = """
        You are an expert AI Digital Marketing Strategist. Generate a highly practical,
        data-driven, and customized 30-day marketing strategy based on the business inputs provided.

        You must respond in valid JSON only. No markdown, no extra text. Use this exact structure:
        {
          "positioning": {
            "uvp": "string",
            "messaging": "string",
            "voice": "string"
          },
          "weeks": [
            {
              "week": 1,
              "title": "Foundation & Awareness",
              "setup": "string",
              "contentTypes": ["string"],
              "postingFrequency": "string",
              "funnelSetup": "string",
              "paidStrategy": "string"
            },
            {
              "week": 2,
              "title": "Engagement & Trust Building",
              "themes": ["string"],
              "storytelling": "string",
              "community": "string",
              "leadMagnet": "string"
            },
            {
              "week": 3,
              "title": "Lead Generation & Conversion Push",
              "conversionCampaign": "string",
              "retargeting": "string",
              "offers": ["string"],
              "emailSequence": ["string"]
            },
            {
              "week": 4,
              "title": "Scaling & Optimization",
              "performanceActions": ["string"],
              "budgetAdjustment": "string",
              "amplification": "string",
              "partnerships": ["string"]
            }
          ],
          "contentPlan": [
            {
              "week": 1,
              "ideas": [
                { "title": "string", "caption": "string", "cta": "string", "hashtags": ["string"] }
              ]
            }
          ],
          "paidStrategy": {
            "objective": "string",
            "targeting": "string",
            "creativeAngles": ["string"],
            "budgetAllocation": [{ "platform": "string", "percentage": 0, "rationale": "string" }]
          },
          "kpis": {
            "metrics": [{ "platform": "string", "metrics": ["string"] }],
            "weeklyBenchmarks": ["string"],
            "conversionExpectations": "string"
          },
          "tools": {
            "scheduling": [{ "name": "string", "reason": "string" }],
            "email": [{ "name": "string", "reason": "string" }],
            "analytics": [{ "name": "string", "reason": "string" }],
            "adManagement": [{ "name": "string", "reason": "string" }]
          }
        }
        Make it highly specific and actionable. 5 content ideas per week.
        """;

    public String generateStrategy(MarketingStrategyRequest request) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_ANTHROPIC_API_KEY_HERE")) {
            return generateFallbackStrategy(request);
        }

        try {
            String userPrompt = buildPrompt(request);

            Map<String, Object> body = Map.of(
                "model", "claude-sonnet-4-20250514",
                "max_tokens", 8000,
                "system", SYSTEM_PROMPT,
                "messages", List.of(Map.of("role", "user", "content", userPrompt))
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            System.out.println("Calling Anthropic Claude API...");
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map> content = (List<Map>) response.getBody().get("content");
                String result = (String) content.get(0).get("text");
                System.out.println("Strategy generated successfully");
                return result;
            }
        } catch (Exception e) {
            System.err.println("Anthropic API Error: " + e.getMessage());
            e.printStackTrace();
        }

        return generateFallbackStrategy(request);
    }

    private String buildPrompt(MarketingStrategyRequest request) {
        return String.format("""
            Generate a 30-day marketing strategy for:
            - Business Name: %s
            - Category: %s
            - Description: %s
            - Target Audience: %s
            - Age Range: %s
            - Pain Points: %s
            - Primary Goal: %s
            - Budget Level: %s
            - Geographic Target: %s
            - Preferred Platforms: %s
            """,
            request.getBusinessName(),
            request.getCategory() != null ? request.getCategory() : request.getBusinessCategory(),
            request.getDescription() != null ? request.getDescription() : "Not provided",
            request.getTargetAudience(),
            request.getAgeRange() != null ? request.getAgeRange() : "Not specified",
            request.getPain() != null ? request.getPain() : "Not specified",
            request.getGoal() != null ? request.getGoal() : request.getPrimaryGoal(),
            request.getBudget() != null ? request.getBudget() : request.getBudgetLevel(),
            request.getGeo() != null ? request.getGeo() : "Not specified",
            String.join(", ", request.getPlatforms())
        );
    }

    private String generateFallbackStrategy(MarketingStrategyRequest request) {
        return String.format("""
            {
              "positioning": {
                "uvp": "%s stands out by delivering exceptional value to %s",
                "messaging": "Focus on solving key pain points with authentic, value-driven content",
                "voice": "Professional, engaging, and customer-centric"
              },
              "weeks": [
                {
                  "week": 1,
                  "title": "Foundation & Awareness",
                  "setup": "Set up profiles, optimize bios, create content calendar",
                  "contentTypes": ["Educational posts", "Behind-the-scenes", "Value propositions"],
                  "postingFrequency": "3-4 posts per week on %s",
                  "funnelSetup": "Create landing page and lead capture forms",
                  "paidStrategy": "Start with %s budget focusing on awareness campaigns"
                },
                {
                  "week": 2,
                  "title": "Engagement & Trust Building",
                  "themes": ["Customer success stories", "Industry insights", "Interactive content"],
                  "storytelling": "Share authentic brand stories and customer testimonials",
                  "community": "Engage with followers, respond to comments, join relevant groups",
                  "leadMagnet": "Create valuable free resource (guide, checklist, or template)"
                },
                {
                  "week": 3,
                  "title": "Lead Generation & Conversion Push",
                  "conversionCampaign": "Launch targeted campaign for %s",
                  "retargeting": "Set up pixel tracking and retargeting ads for website visitors",
                  "offers": ["Limited-time discount", "Free consultation", "Exclusive bundle"],
                  "emailSequence": ["Welcome email", "Value email", "Social proof", "Offer email", "Urgency email"]
                },
                {
                  "week": 4,
                  "title": "Scaling & Optimization",
                  "performanceActions": ["Analyze top-performing content", "Double down on winning ads", "A/B test variations"],
                  "budgetAdjustment": "Increase budget on best-performing platforms by 20-30%%",
                  "amplification": "Boost top organic posts, collaborate with micro-influencers",
                  "partnerships": ["Industry complementary businesses", "Local community groups", "Affiliate partners"]
                }
              ],
              "contentPlan": [
                {
                  "week": 1,
                  "ideas": [
                    { "title": "Introduction Post", "caption": "Meet %s! We're here to help %s achieve their goals.", "cta": "Follow for tips", "hashtags": ["business", "marketing", "growth"] },
                    { "title": "Value Proposition", "caption": "Why choose us? Here's what makes us different...", "cta": "Learn more", "hashtags": ["value", "quality", "service"] },
                    { "title": "Behind the Scenes", "caption": "Take a peek at how we work our magic!", "cta": "Comment below", "hashtags": ["behindthescenes", "team", "process"] },
                    { "title": "Customer Pain Point", "caption": "Struggling with [problem]? You're not alone. Here's how we can help.", "cta": "DM us", "hashtags": ["solutions", "help", "support"] },
                    { "title": "Educational Tip", "caption": "Pro tip: [Valuable insight for your audience]", "cta": "Save this", "hashtags": ["tips", "advice", "howto"] }
                  ]
                },
                {
                  "week": 2,
                  "ideas": [
                    { "title": "Success Story", "caption": "How we helped [customer] achieve [result]", "cta": "Read more", "hashtags": ["success", "results", "testimonial"] },
                    { "title": "Industry Insight", "caption": "Latest trends in [industry] you need to know", "cta": "Share your thoughts", "hashtags": ["trends", "industry", "insights"] },
                    { "title": "Q&A Session", "caption": "Your questions answered! Ask us anything.", "cta": "Comment questions", "hashtags": ["qa", "askme", "community"] },
                    { "title": "User-Generated Content", "caption": "Love seeing our customers succeed! Repost from @customer", "cta": "Tag us", "hashtags": ["community", "customers", "love"] },
                    { "title": "Free Resource", "caption": "Download our free [guide/checklist] - link in bio!", "cta": "Get it now", "hashtags": ["free", "resource", "download"] }
                  ]
                },
                {
                  "week": 3,
                  "ideas": [
                    { "title": "Special Offer", "caption": "Limited time: [X%% off / special deal] for new customers!", "cta": "Shop now", "hashtags": ["sale", "offer", "limited"] },
                    { "title": "Social Proof", "caption": "Join [X] happy customers who trust us!", "cta": "See reviews", "hashtags": ["reviews", "trust", "proof"] },
                    { "title": "Comparison Post", "caption": "Us vs. Them: Why we're the better choice", "cta": "Learn why", "hashtags": ["comparison", "value", "choice"] },
                    { "title": "Urgency Post", "caption": "Only [X] spots left! Don't miss out.", "cta": "Book now", "hashtags": ["urgent", "limited", "act"] },
                    { "title": "Case Study", "caption": "Deep dive: How we achieved [specific result]", "cta": "Read full story", "hashtags": ["casestudy", "results", "data"] }
                  ]
                },
                {
                  "week": 4,
                  "ideas": [
                    { "title": "Top Performer", "caption": "Our most popular [product/service] - here's why", "cta": "Try it", "hashtags": ["popular", "bestseller", "favorite"] },
                    { "title": "Customer Spotlight", "caption": "Meet [customer name] and their success story", "cta": "Be next", "hashtags": ["spotlight", "customer", "success"] },
                    { "title": "Future Plans", "caption": "Exciting things coming next month! Stay tuned.", "cta": "Turn on notifications", "hashtags": ["comingsoon", "excited", "future"] },
                    { "title": "Thank You Post", "caption": "Grateful for this amazing community! Thank you for your support.", "cta": "Share love", "hashtags": ["thankyou", "grateful", "community"] },
                    { "title": "Call to Action", "caption": "Ready to [achieve goal]? Let's make it happen together!", "cta": "Get started", "hashtags": ["action", "start", "now"] }
                  ]
                }
              ],
              "paidStrategy": {
                "objective": "Drive %s with targeted campaigns",
                "targeting": "Target %s based on interests, behaviors, and demographics",
                "creativeAngles": ["Problem-solution format", "Before/after transformations", "Customer testimonials", "Limited-time offers"],
                "budgetAllocation": [
                  { "platform": "%s", "percentage": 40, "rationale": "Primary platform with highest engagement" },
                  { "platform": "Facebook", "percentage": 30, "rationale": "Broad reach and retargeting capabilities" },
                  { "platform": "Google Ads", "percentage": 30, "rationale": "Capture high-intent search traffic" }
                ]
              },
              "kpis": {
                "metrics": [
                  { "platform": "Instagram", "metrics": ["Reach", "Engagement Rate", "Profile Visits", "Link Clicks"] },
                  { "platform": "Facebook", "metrics": ["Post Reach", "Page Likes", "Click-Through Rate", "Conversions"] },
                  { "platform": "Email", "metrics": ["Open Rate", "Click Rate", "Conversion Rate", "List Growth"] }
                ],
                "weeklyBenchmarks": [
                  "Week 1: 1000+ reach, 50+ engagements",
                  "Week 2: 2000+ reach, 100+ engagements, 20+ leads",
                  "Week 3: 3000+ reach, 150+ engagements, 50+ leads, 5+ conversions",
                  "Week 4: 5000+ reach, 250+ engagements, 100+ leads, 15+ conversions"
                ],
                "conversionExpectations": "Target 2-5%% conversion rate from leads to customers by end of month"
              },
              "tools": {
                "scheduling": [
                  { "name": "Buffer", "reason": "Easy scheduling across multiple platforms" },
                  { "name": "Later", "reason": "Visual Instagram planning and analytics" }
                ],
                "email": [
                  { "name": "Mailchimp", "reason": "User-friendly with automation features" },
                  { "name": "ConvertKit", "reason": "Great for creators and small businesses" }
                ],
                "analytics": [
                  { "name": "Google Analytics", "reason": "Track website traffic and conversions" },
                  { "name": "Meta Business Suite", "reason": "Unified Facebook and Instagram insights" }
                ],
                "adManagement": [
                  { "name": "Facebook Ads Manager", "reason": "Comprehensive ad creation and tracking" },
                  { "name": "Google Ads", "reason": "Search and display advertising" }
                ]
              }
            }
            """,
            request.getBusinessName(),
            request.getTargetAudience(),
            String.join(", ", request.getPlatforms()),
            request.getBudgetLevel() != null ? request.getBudgetLevel() : request.getBudget(),
            request.getPrimaryGoal() != null ? request.getPrimaryGoal() : request.getGoal(),
            request.getBusinessName(),
            request.getTargetAudience(),
            request.getTargetAudience(),
            request.getPrimaryGoal() != null ? request.getPrimaryGoal() : request.getGoal(),
            request.getPlatforms().isEmpty() ? "Instagram" : request.getPlatforms().get(0)
        );
    }
}
