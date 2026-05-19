package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    @GetMapping("/api/alarm/config")
    public ApiResponse<Map<String, Object>> listAlarmConfig() {
        Map<String, Object> data = new HashMap<>();
        data.put("rows", new ArrayList<>());
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/api/sysConfig/{id}")
    public ApiResponse<Map<String, Object>> getSysConfig(@PathVariable Long id) {
        return ApiResponse.success(new HashMap<>());
    }

    @PostMapping("/api/alarm/config")
    public ApiResponse<String> addAlarmConfig(@RequestBody Map<String, Object> config) {
        return ApiResponse.success("Alarm config added successfully");
    }

    @PutMapping("/api/sysConfig")
    public ApiResponse<String> updateSysConfig(@RequestBody Map<String, Object> config) {
        return ApiResponse.success("Config updated successfully");
    }

    @DeleteMapping("/api/alarm/config/{id}")
    public ApiResponse<String> deleteAlarmConfig(@PathVariable Long id) {
        return ApiResponse.success("Alarm config deleted successfully");
    }
}