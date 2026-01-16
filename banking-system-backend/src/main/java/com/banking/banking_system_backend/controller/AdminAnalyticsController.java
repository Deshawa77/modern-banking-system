package com.banking.banking_system_backend.controller;

import com.banking.banking_system_backend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/analytics")
public class AdminAnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/summary")
    public Map<String, Object> getAnalyticsSummary() {
        return analyticsService.getSummary();
    }
}
