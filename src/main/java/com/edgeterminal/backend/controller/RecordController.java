package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class RecordController {

    @GetMapping("/api/alarm/record")
    public ApiResponse<Map<String, Object>> listAlarmRecords(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new ArrayList<>());
        data.put("totalCount", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/alarm/record/{id}")
    public ApiResponse<Map<String, Object>> getAlarmRecord(@PathVariable Long id) {
        return ApiResponse.success(new HashMap<>());
    }

    @PutMapping("/alarm/record")
    public ApiResponse<String> updateAlarmRecord(@RequestBody Map<String, Object> record) {
        return ApiResponse.success("Record updated successfully");
    }

    @DeleteMapping("/alarm/record/{id}")
    public ApiResponse<String> deleteAlarmRecord(@PathVariable Long id) {
        return ApiResponse.success("Record deleted successfully");
    }

    @GetMapping("/access/record")
    public ApiResponse<Map<String, Object>> listAccessRecords(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new ArrayList<>());
        data.put("totalCount", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/access/record/{id}")
    public ApiResponse<Map<String, Object>> getAccessRecord(@PathVariable Long id) {
        return ApiResponse.success(new HashMap<>());
    }

    @PutMapping("/access/record")
    public ApiResponse<String> updateAccessRecord(@RequestBody Map<String, Object> record) {
        return ApiResponse.success("Access record updated successfully");
    }

    @DeleteMapping("/access/record/{id}")
    public ApiResponse<String> deleteAccessRecord(@PathVariable Long id) {
        return ApiResponse.success("Access record deleted successfully");
    }
}