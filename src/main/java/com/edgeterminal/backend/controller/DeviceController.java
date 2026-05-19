package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    @GetMapping
    public ApiResponse<Map<String, Object>> listDevices(
            @RequestParam(required = false) String deviceName,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new ArrayList<>());
        data.put("totalCount", 0);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        return ApiResponse.success(data);
    }

    @GetMapping("/queryAll")
    public ApiResponse<List<Object>> queryAll() {
        return ApiResponse.success(new ArrayList<>());
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getDevice(@PathVariable Long id) {
        return ApiResponse.success(new HashMap<>());
    }

    @PostMapping
    public ApiResponse<String> addDevice(@RequestBody Map<String, Object> device) {
        return ApiResponse.success("Device added successfully");
    }

    @PutMapping
    public ApiResponse<String> updateDevice(@RequestBody Map<String, Object> device) {
        return ApiResponse.success("Device updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteDevice(@PathVariable Long id) {
        return ApiResponse.success("Device deleted successfully");
    }

    @GetMapping("/deviceTask/audioStatus")
    public ApiResponse<String> setAudioStatus(@RequestParam String status) {
        return ApiResponse.success("Audio status updated");
    }

    @GetMapping("/deviceTask/getAudioStatus")
    public ApiResponse<Map<String, Object>> getAudioStatus() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "0");
        return ApiResponse.success(data);
    }
}