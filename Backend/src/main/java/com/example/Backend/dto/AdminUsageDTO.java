package com.example.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUsageDTO {
    private String userName;
    private long contentCount;
    private long imageCount;
    private String month;
    private int usage;
}
