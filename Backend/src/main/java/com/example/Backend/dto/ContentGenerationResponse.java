package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ContentGenerationResponse {
    private List<String> captions;
    private List<String> hashtags;
    private List<String> postIdeas;
    private List<ContentCalendarItem> contentCalendar;
    
    @Data
    @AllArgsConstructor
    public static class ContentCalendarItem {
        private String day;
        private String postType;
        private String caption;
        private String hashtags;
    }
}
