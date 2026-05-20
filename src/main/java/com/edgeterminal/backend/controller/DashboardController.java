package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.Alert;
import com.edgeterminal.backend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cockpit")
@RequiredArgsConstructor
public class DashboardController {

    private final AlertRepository alertRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/getTodayOverview")
    public ApiResponse<Map<String, Object>> getTodayOverview(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        List<Alert> alerts = getAlertsInRange(startTime, endTime);

        long total = alerts.size();
        long processed = alerts.stream().filter(a -> a.getStatus() != null && a.getStatus() == 1).count();
        long unprocessed = alerts.stream().filter(a -> a.getStatus() == null || a.getStatus() == 0).count();

        Map<String, Object> data = new HashMap<>();
        data.put("totalEvents", total);
        data.put("todayEvent", total);
        data.put("processedEvents", processed);
        data.put("todayEventClosed", processed);
        data.put("unprocessedEvents", unprocessed);
        data.put("todayEventUnHandled", unprocessed);
        data.put("todayEventHandled", processed);
        data.put("peopleCount", total);
        data.put("vehicleCount", 0);
        data.put("peopleNum", total);
        data.put("vehiclesNum", 0);

        return ApiResponse.success(data);
    }

    @GetMapping("/getVehiclesNumber")
    public ApiResponse<Map<String, Object>> getVehiclesNumber(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String type) {

        List<Alert> alerts = getAlertsInRange(startTime, endTime);

        Map<String, Object> data = new HashMap<>();
        data.put("peopleNum", alerts.size());
        data.put("vehiclesNum", 0);

        return ApiResponse.success(data);
    }

    @GetMapping("/getVehicles")
    public ApiResponse<List<Map<String, Object>>> getVehicles(
            @RequestParam(required = false) String type) {
        return ApiResponse.success(new ArrayList<>());
    }

    @GetMapping("/getTrendChart")
    public ApiResponse<List<Map<String, Object>>> getTrendChart(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String type) {

        List<Alert> alerts = getAlertsInRange(startTime, endTime);

        List<Map<String, Object>> result = new ArrayList<>();

        // Group by violation type
        Map<String, List<Alert>> byType = alerts.stream()
                .filter(a -> a.getViolationType() != null)
                .collect(Collectors.groupingBy(Alert::getViolationType));

        byType.forEach((violationType, typeAlerts) -> {
            // Group by time slot
            Map<Integer, Long> byTime;

            if ("2".equals(type)) {
                // By day of week (1-7)
                byTime = typeAlerts.stream()
                        .filter(a -> a.getCreateTime() != null)
                        .collect(Collectors.groupingBy(
                                a -> a.getCreateTime().getDayOfWeek().getValue(),
                                Collectors.counting()
                        ));
            } else if ("3".equals(type)) {
                // By day of month
                byTime = typeAlerts.stream()
                        .filter(a -> a.getCreateTime() != null)
                        .collect(Collectors.groupingBy(
                                a -> a.getCreateTime().getDayOfMonth(),
                                Collectors.counting()
                        ));
            } else {
                // By hour (default - today)
                byTime = typeAlerts.stream()
                        .filter(a -> a.getCreateTime() != null)
                        .collect(Collectors.groupingBy(
                                a -> a.getCreateTime().getHour(),
                                Collectors.counting()
                        ));
            }

            // Add summary item (for pie chart)
            Map<String, Object> summary = new HashMap<>();
            summary.put("eventTypeId", violationType);
            summary.put("num", (long) typeAlerts.size());
            summary.put("time", -1); // -1 indicates summary
            summary.put("name", violationType);
            result.add(summary);

            // Add time-based items (for line chart)
            byTime.forEach((time, count) -> {
                Map<String, Object> item = new HashMap<>();
                item.put("eventTypeId", violationType);
                item.put("num", count);
                item.put("time", time);
                item.put("name", violationType);
                result.add(item);
            });
        });

        return ApiResponse.success(result);
    }

    @GetMapping("/getDeviceTrendChart")
    public ApiResponse<List<Map<String, Object>>> getDeviceTrendChart(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String type) {

        List<Alert> alerts = getAlertsInRange(startTime, endTime);

        // Group by device
        Map<String, List<Alert>> byDevice = alerts.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getDeviceName() != null ? a.getDeviceName() : "Unknown Device"
                ));

        List<Map<String, Object>> result = new ArrayList<>();

        byDevice.forEach((deviceName, deviceAlerts) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("deviceName", deviceName);
            item.put("deviceId", deviceAlerts.get(0).getDeviceId());

            // Build alarm list by hour (24 slots)
            List<Integer> alarmList = new ArrayList<>(Collections.nCopies(24, 0));
            deviceAlerts.forEach(a -> {
                if (a.getCreateTime() != null) {
                    int hour = a.getCreateTime().getHour();
                    alarmList.set(hour, alarmList.get(hour) + 1);
                }
            });

            item.put("deviceAlarmList", alarmList);
            item.put("total", deviceAlerts.size());
            result.add(item);
        });

        // Sort by total descending, take top 3
        result.sort((a, b) -> Integer.compare(
                (Integer) b.get("total"),
                (Integer) a.get("total")
        ));

        return ApiResponse.success(result.stream().limit(3).collect(Collectors.toList()));
    }

    private List<Alert> getAlertsInRange(String startTime, String endTime) {
        if (startTime != null && endTime != null) {
            try {
                String cleanStart = startTime.replace("T", " ").trim();
                String cleanEnd = endTime.replace("T", " ").trim();
                LocalDateTime start = LocalDateTime.parse(cleanStart, formatter);
                LocalDateTime end = LocalDateTime.parse(cleanEnd, formatter);
                return alertRepository.findAll().stream()
                        .filter(a -> a.getCreateTime() != null
                                && !a.getCreateTime().isBefore(start)
                                && !a.getCreateTime().isAfter(end))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                return alertRepository.findAll();
            }
        }
        return alertRepository.findAll();
    }
}