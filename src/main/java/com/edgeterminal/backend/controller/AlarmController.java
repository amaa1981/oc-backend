package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final SystemConfigService configService;

    @GetMapping("/api/alarm/config")
    public ApiResponse<Map<String, Object>> listAlarmConfig(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(configService.listPush("http", pageNum, pageSize));
    }

    @PostMapping("/api/alarm/config")
    public ApiResponse<String> addAlarmConfig(@RequestBody Map<String, Object> config) {
        return ApiResponse.success("Alarm config added successfully");
    }

    @DeleteMapping("/api/alarm/config/{id}")
    public ApiResponse<String> deleteAlarmConfig(@PathVariable Long id) {
        return ApiResponse.success("Alarm config deleted successfully");
    }
}
