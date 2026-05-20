package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    // Sample devices
    private static final List<Map<String, Object>> devices = new ArrayList<>(Arrays.asList(
        createDevice(1L, "Kitchen Camera 1", "CAM-001", "Main Kitchen", "online"),
        createDevice(2L, "Kitchen Camera 2", "CAM-002", "Prep Area", "online"),
        createDevice(3L, "Kitchen Camera 3", "CAM-003", "Storage Room", "offline"),
        createDevice(4L, "Kitchen Camera 4", "CAM-004", "Entrance", "online")
    ));

    private static Map<String, Object> createDevice(Long id, String name, String deviceId, String location, String status) {
        Map<String, Object> d = new HashMap<>();
        d.put("id", id);
        d.put("name", name);
        d.put("deviceId", deviceId);
        d.put("location", location);
        d.put("status", status);
        d.put("ip", "192.168.1." + id);
        d.put("createTime", "2026-01-01 00:00:00");
        return d;
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> listDevice(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", devices);
        data.put("rows", devices);
        data.put("totalCount", devices.size());
        data.put("total", devices.size());
        return ApiResponse.success(data);
    }

    @GetMapping("/queryAll")
    public ApiResponse<List<Map<String, Object>>> listDeviceAll() {
        return ApiResponse.success(devices);
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getDevice(@PathVariable Long id) {
        return devices.stream()
                .filter(d -> d.get("id").equals(id))
                .findFirst()
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Device not found"));
    }

    @PostMapping
    public ApiResponse<String> addDevice(@RequestBody Map<String, Object> device) {
        device.put("id", devices.size() + 1L);
        devices.add(device);
        return ApiResponse.success("Device added successfully");
    }

    @PutMapping
    public ApiResponse<String> updateDevice(@RequestBody Map<String, Object> device) {
        return ApiResponse.success("Device updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteDevice(@PathVariable Long id) {
        devices.removeIf(d -> d.get("id").equals(id));
        return ApiResponse.success("Device deleted successfully");
    }

    @GetMapping("/taskStatus/audioStatus")
    public ApiResponse<String> audioStatus(@RequestParam String status) {
        return ApiResponse.success("Audio status updated");
    }

    @GetMapping("/taskStatus/getAudioStatus")
    public ApiResponse<Map<String, Object>> getAudioStatus() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", 10);
        return ApiResponse.success(data);
    }
}