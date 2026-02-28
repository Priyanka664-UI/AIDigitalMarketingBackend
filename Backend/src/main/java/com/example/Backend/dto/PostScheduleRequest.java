package com.example.Backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostScheduleRequest {
    private Long campaignId;
    private String platform;
    private String caption;
    private String hashtags;
    private String imageUrl;
    private LocalDateTime scheduledTime;
}
