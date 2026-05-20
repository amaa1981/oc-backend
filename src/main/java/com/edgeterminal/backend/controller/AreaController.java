package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/area")
@RequiredArgsConstructor
public class AreaController {

    @GetMapping
    public ApiResponse<Map<String, Object>> listArea(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new ArrayList<>());
        data.put("totalCount", 0);
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getArea(@PathVariable Long id) {
        return ApiResponse.success(new HashMap<>());
    }

    @PostMapping
    public ApiResponse<String> addArea(@RequestBody Map<String, Object> area) {
        return ApiResponse.success("Area added successfully");
    }

    @PutMapping
    public ApiResponse<String> updateArea(@RequestBody Map<String, Object> area) {
        return ApiResponse.success("Area updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteArea(@PathVariable Long id) {
        return ApiResponse.success("Area deleted successfully");
    }
}