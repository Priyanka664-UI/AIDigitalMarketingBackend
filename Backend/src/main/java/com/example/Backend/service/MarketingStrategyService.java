package com.example.Backend.service;

import com.example.Backend.dto.MarketingStrategyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketingStrategyService {

    @Autowired
    private AnthropicService anthropicService;

    public String generateMarketingStrategy(MarketingStrategyRequest request) {
        return anthropicService.generateStrategy(request);
    }
}
