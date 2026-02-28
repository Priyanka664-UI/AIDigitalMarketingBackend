package com.example.Backend.dto;

import com.example.Backend.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPostDTO {
    private Long id;
    private String userName;
    private String platform;
    private String caption;
    private LocalDateTime scheduledTime;
    private String status;
}
