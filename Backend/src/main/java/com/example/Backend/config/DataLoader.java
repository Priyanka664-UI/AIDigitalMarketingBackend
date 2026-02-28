package com.example.Backend.config;

import com.example.Backend.model.*;
import com.example.Backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final CampaignRepository campaignRepository;
    private final PostRepository postRepository;

    public DataLoader(UserRepository userRepository, BusinessRepository businessRepository,
                      CampaignRepository campaignRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.campaignRepository = campaignRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) {
        // Users
        User user1 = new User();
        user1.setBusinessName("TechStart Solutions");
        user1.setCategory("Technology");
        user1.setTargetAudience("Small businesses and startups");
        user1.setBrandTone("PROFESSIONAL");
        user1.setContact("admin@techstart.com");
        user1.setPassword("password123");
        userRepository.save(user1);

        User user2 = new User();
        user2.setBusinessName("Fashion Hub");
        user2.setCategory("Fashion & Retail");
        user2.setTargetAudience("Young adults 18-35");
        user2.setBrandTone("CASUAL");
        user2.setContact("contact@fashionhub.com");
        user2.setPassword("password123");
        userRepository.save(user2);

        // Businesses
        Business business1 = new Business();
        business1.setBusinessName("TechStart Solutions");
        business1.setProductDetails("Cloud-based SaaS solutions for small businesses");
        business1.setTargetAudience("SMB owners, IT managers");
        business1.setBrandTone(Business.BrandTone.PROFESSIONAL);
        business1.setMarketingGoal(Business.MarketingGoal.LEADS);
        business1.setIndustry("Technology");
        businessRepository.save(business1);

        Business business2 = new Business();
        business2.setBusinessName("Fashion Hub");
        business2.setProductDetails("Trendy clothing and accessories");
        business2.setTargetAudience("Fashion-conscious millennials");
        business2.setBrandTone(Business.BrandTone.CASUAL);
        business2.setMarketingGoal(Business.MarketingGoal.SALES);
        business2.setIndustry("Fashion");
        businessRepository.save(business2);

        Business business3 = new Business();
        business3.setBusinessName("Gourmet Delights");
        business3.setProductDetails("Premium organic food products");
        business3.setTargetAudience("Health-conscious consumers");
        business3.setBrandTone(Business.BrandTone.PREMIUM);
        business3.setMarketingGoal(Business.MarketingGoal.AWARENESS);
        business3.setIndustry("Food & Beverage");
        businessRepository.save(business3);

        // Campaigns
        Campaign campaign1 = new Campaign();
        campaign1.setBusiness(business1);
        campaign1.setName("Q1 Product Launch");
        campaign1.setStartDate(LocalDate.now());
        campaign1.setEndDate(LocalDate.now().plusMonths(3));
        campaign1.setStatus(Campaign.CampaignStatus.ACTIVE);
        campaignRepository.save(campaign1);

        Campaign campaign2 = new Campaign();
        campaign2.setBusiness(business2);
        campaign2.setName("Summer Collection 2024");
        campaign2.setStartDate(LocalDate.now().minusDays(15));
        campaign2.setEndDate(LocalDate.now().plusMonths(2));
        campaign2.setStatus(Campaign.CampaignStatus.ACTIVE);
        campaignRepository.save(campaign2);

        Campaign campaign3 = new Campaign();
        campaign3.setBusiness(business3);
        campaign3.setName("Organic Awareness Drive");
        campaign3.setStartDate(LocalDate.now().plusDays(7));
        campaign3.setEndDate(LocalDate.now().plusMonths(1));
        campaign3.setStatus(Campaign.CampaignStatus.DRAFT);
        campaignRepository.save(campaign3);

        // Posts
        Post post1 = new Post();
        post1.setCampaign(campaign1);
        post1.setPlatform(Post.Platform.LINKEDIN);
        post1.setCaption("Excited to announce our new cloud solution! Transform your business with cutting-edge technology. 🚀");
        post1.setHashtags("#CloudComputing #SaaS #BusinessGrowth #TechInnovation");
        post1.setScheduledTime(LocalDateTime.now().plusDays(1));
        post1.setStatus(Post.PostStatus.SCHEDULED);
        post1.setLikes(245);
        post1.setShares(67);
        post1.setComments(34);
        post1.setReach(3200);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setCampaign(campaign1);
        post2.setPlatform(Post.Platform.FACEBOOK);
        post2.setCaption("Small business owners, this is for you! Our platform simplifies your operations. Try it free! 💼");
        post2.setHashtags("#SmallBusiness #Productivity #Automation");
        post2.setScheduledTime(LocalDateTime.now().plusDays(2));
        post2.setStatus(Post.PostStatus.SCHEDULED);
        post2.setLikes(189);
        post2.setShares(45);
        post2.setComments(28);
        post2.setReach(2800);
        postRepository.save(post2);

        Post post3 = new Post();
        post3.setCampaign(campaign2);
        post3.setPlatform(Post.Platform.INSTAGRAM);
        post3.setCaption("Summer vibes are here! ☀️ Check out our latest collection. Limited stock available! 👗✨");
        post3.setHashtags("#SummerFashion #OOTD #FashionHub #TrendyStyle");
        post3.setScheduledTime(LocalDateTime.now().minusDays(5));
        post3.setStatus(Post.PostStatus.PUBLISHED);
        post3.setLikes(1523);
        post3.setShares(234);
        post3.setComments(156);
        post3.setReach(8900);
        postRepository.save(post3);

        Post post4 = new Post();
        post4.setCampaign(campaign2);
        post4.setPlatform(Post.Platform.FACEBOOK);
        post4.setCaption("New arrivals alert! 🛍️ Shop the hottest trends of the season. Free shipping on orders over $50!");
        post4.setHashtags("#Fashion #Shopping #NewArrivals #Style");
        post4.setScheduledTime(LocalDateTime.now().minusDays(3));
        post4.setStatus(Post.PostStatus.PUBLISHED);
        post4.setLikes(987);
        post4.setShares(178);
        post4.setComments(92);
        post4.setReach(6500);
        postRepository.save(post4);

        Post post5 = new Post();
        post5.setCampaign(campaign2);
        post5.setPlatform(Post.Platform.WHATSAPP);
        post5.setCaption("Exclusive offer for our VIP customers! Get 20% off your next purchase. Use code: SUMMER20");
        post5.setHashtags("#ExclusiveOffer #VIPSale");
        post5.setScheduledTime(LocalDateTime.now().minusDays(1));
        post5.setStatus(Post.PostStatus.PUBLISHED);
        post5.setLikes(456);
        post5.setShares(89);
        post5.setComments(67);
        post5.setReach(3400);
        postRepository.save(post5);

        System.out.println("✅ Test data loaded successfully!");
        System.out.println("📊 Created: 2 Users, 3 Businesses, 3 Campaigns, 5 Posts");
    }
}
