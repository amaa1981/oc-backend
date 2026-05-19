package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cockpit")
@RequiredArgsConstructor
public class DashboardController {

    // GET /api/cockpit/getTodayOverview
    @GetMapping("/getTodayOverview")
    public ApiResponse<Map<String, Object>> getTodayOverview() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalEvents", 0);
        data.put("processedEvents", 0);
        data.put("unprocessedEvents", 0);
        data.put("peopleCount", 0);
        data.put("vehicleCount", 0);
        return ApiResponse.success(data);
    }

    // GET /api/cockpit/getTrendChart
    @GetMapping("/getTrendChart")
    public ApiResponse<Map<String, Object>> getTrendChart(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Map<String, Object> data = new HashMap<>();
        data.put("times", new ArrayList<>());
        data.put("values", new ArrayList<>());
        return ApiResponse.success(data);
    }

    // GET /api/cockpit/getDeviceTrendChart
    @GetMapping("/getDeviceTrendChart")
    public ApiResponse<Map<String, Object>> getDeviceTrendChart(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Map<String, Object> data = new HashMap<>();
        data.put("times", new ArrayList<>());
        data.put("values", new ArrayList<>());
        return ApiResponse.success(data);
    }

    // GET /api/cockpit/getVehiclesNumber
    @GetMapping("/getVehiclesNumber")
    public ApiResponse<Map<String, Object>> getVehiclesNumber(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Map<String, Object> data = new HashMap<>();
        data.put("peopleCount", 0);
        data.put("vehicleCount", 0);
        return ApiResponse.success(data);
    }

    // GET /api/cockpit/getVehicles
    @GetMapping("/getVehicles")
    public ApiResponse<Map<String, Object>> getVehicles(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        Map<String, Object> data = new HashMap<>();
        data.put("times", new ArrayList<>());
        data.put("peopleValues", new ArrayList<>());
        data.put("vehicleValues", new ArrayList<>());
        return ApiResponse.success(data);
    }
}