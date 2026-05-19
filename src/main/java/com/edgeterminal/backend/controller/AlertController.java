package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.Alert;
import com.edgeterminal.backend.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    // POST /api/ai/alert — receive alert from inference service
    @PostMapping("/alert")
    public ApiResponse<Alert> createAlert(@RequestBody Alert alert) {
        try {
            Alert saved = alertService.createAlert(alert);
            return ApiResponse.success(saved);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    // GET /api/ai/alerts/recent — get recent alerts for dashboard
    @GetMapping("/alerts/recent")
    public ApiResponse<List<Alert>> getRecentAlerts() {
        return ApiResponse.success(alertService.getRecentAlerts());
    }

    // GET /api/ai/alerts/unprocessed — get unprocessed alerts
    @GetMapping("/alerts/unprocessed")
    public ApiResponse<List<Alert>> getUnprocessedAlerts() {
        return ApiResponse.success(alertService.getUnprocessedAlerts());
    }

    // PUT /api/ai/alert/{id}/process — mark alert as processed
    @PutMapping("/alert/{id}/process")
    public ApiResponse<Alert> processAlert(@PathVariable Long id) {
        try {
            Alert alert = alertService.processAlert(id);
            return ApiResponse.success(alert);
        } catch (Exception e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    // GET /api/ai/alerts/stats — get alert statistics
    @GetMapping("/alerts/stats")
    public ApiResponse<Map<String, Long>> getAlertStats() {
        return ApiResponse.success(alertService.getAlertStats());
    }
}