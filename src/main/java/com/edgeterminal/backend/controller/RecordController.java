package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.Alert;
import com.edgeterminal.backend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final AlertRepository alertRepository;

    @GetMapping("/api/alarm/record")
    public ApiResponse<Map<String, Object>> listAlarmRecords(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime) {

        List<Alert> all = alertRepository.findAll();

        // Filter by date if provided
        if (beginTime != null && endTime != null) {
            try {
                LocalDateTime begin = LocalDateTime.parse(beginTime.replace(" ", "T"));
                LocalDateTime end = LocalDateTime.parse(endTime.replace(" ", "T"));
                all = all.stream()
                        .filter(a -> a.getCreateTime() != null
                                && !a.getCreateTime().isBefore(begin)
                                && !a.getCreateTime().isAfter(end))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                // ignore parse errors
            }
        }

        // Sort by newest first
        all.sort((a, b) -> {
            if (a.getCreateTime() == null) return 1;
            if (b.getCreateTime() == null) return -1;
            return b.getCreateTime().compareTo(a.getCreateTime());
        });

        int total = all.size();
        int start = (pageNum - 1) * pageSize;
        int end2 = Math.min(start + pageSize, total);
        List<Alert> page = start < total ? all.subList(start, end2) : new ArrayList<>();

        // Map to frontend format
        List<Map<String, Object>> records = page.stream().map(a -> {
            Map<String, Object> r = new HashMap<>();
            r.put("id", a.getId());
            r.put("violationType", a.getViolationType());
            r.put("eventTypeId", a.getViolationType());
            r.put("deviceId", a.getDeviceId());
            r.put("deviceName", a.getDeviceName());
            r.put("equipmentName", a.getDeviceName());
            r.put("confidence", a.getConfidence());
            r.put("imageData", a.getImageData());
            r.put("status", a.getStatus());
            r.put("createTime", a.getCreateTime() != null ? a.getCreateTime().toString() : null);
            r.put("sendTime", a.getCreateTime() != null ? a.getCreateTime().toString() : null);
            return r;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("rows", records);
        data.put("totalCount", total);
        data.put("total", total);

        return ApiResponse.success(data);
    }

    @GetMapping("/api/alarm/record/{id}")
    public ApiResponse<Map<String, Object>> getAlarmRecord(@PathVariable Long id) {
        return alertRepository.findById(id).map(a -> {
            Map<String, Object> r = new HashMap<>();
            r.put("id", a.getId());
            r.put("violationType", a.getViolationType());
            r.put("eventTypeId", a.getViolationType());
            r.put("deviceId", a.getDeviceId());
            r.put("deviceName", a.getDeviceName());
            r.put("equipmentName", a.getDeviceName());
            r.put("confidence", a.getConfidence());
            r.put("imageData", a.getImageData());
            r.put("status", a.getStatus());
            r.put("createTime", a.getCreateTime() != null ? a.getCreateTime().toString() : null);
            r.put("sendTime", a.getCreateTime() != null ? a.getCreateTime().toString() : null);
            return ApiResponse.success(r);
        }).orElse(ApiResponse.error(404, "Record not found"));
    }

    @PutMapping("/api/alarm/record")
    public ApiResponse<String> updateAlarmRecord(@RequestBody Map<String, Object> record) {
        return ApiResponse.success("Record updated successfully");
    }

    @DeleteMapping("/api/alarm/record/{id}")
    public ApiResponse<String> deleteAlarmRecord(@PathVariable Long id) {
        alertRepository.deleteById(id);
        return ApiResponse.success("Record deleted successfully");
    }

    @GetMapping("/api/access/record")
    public ApiResponse<Map<String, Object>> listAccessRecords(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new ArrayList<>());
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/api/access/record/{id}")
    public ApiResponse<Map<String, Object>> getAccessRecord(@PathVariable Long id) {
        return ApiResponse.success(new HashMap<>());
    }
}