package com.example.Backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "settings")
@Data
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    private String defaultTone = "Professional";
    private String defaultImageStyle = "Realistic";
    private String contentFrequency = "Weekly";
    private Boolean autoSchedule = false;
    private Boolean aiSuggestions = true;
    
    private Boolean emailPost = true;
    private Boolean emailFail = true;
    private Boolean emailWeekly = true;
    private Boolean emailTips = false;
    private Boolean pushPost = true;
    private Boolean pushEngagement = false;
    
    private String defaultPostingTime = "10:00";
    private String timeZone = "UTC";
}
