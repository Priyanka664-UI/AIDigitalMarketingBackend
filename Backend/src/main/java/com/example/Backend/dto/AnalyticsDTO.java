package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsDTO {
    private Map<String, Long> postsPerPlatform;
    private Map<String, Long> usersPerCategory;
    private List<MonthlyTrendDTO> monthlyTrend;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyTrendDTO {
        private String month;
        private long count;
    }
}
