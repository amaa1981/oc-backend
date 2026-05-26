package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.Alert;
import com.edgeterminal.backend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RecordController {

    private final AlertRepository alertRepository;

    private static final List<String> VIOLATION_TYPES = Arrays.asList("no_glove", "no_hairnet", "no_mask");

    @GetMapping("/api/alarm/record")
    public ApiResponse<Map<String, Object>> listAlarmRecords(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String eventTypeId,
            @RequestParam(required = false) String violationType,
            @RequestParam(required = false) String equipmentName,
            @RequestParam(required = false) String status) {

        List<Alert> all = alertRepository.findAll();

        // Only violations
        all = all.stream()
                .filter(a -> VIOLATION_TYPES.contains(a.getViolationType()))
                .collect(Collectors.toList());

        // Filter by eventTypeId/violationType
        String typeFilter = eventTypeId != null ? eventTypeId : violationType;
        if (typeFilter != null && !typeFilter.isEmpty()) {
            final String tf = typeFilter;
            all = all.stream()
                    .filter(a -> tf.equals(a.getViolationType()))
                    .collect(Collectors.toList());
        }

        // Filter by equipment name
        if (equipmentName != null && !equipmentName.isEmpty()) {
            final String ef = equipmentName;
            all = all.stream()
                    .filter(a -> a.getDeviceName() != null && a.getDeviceName().contains(ef))
                    .collect(Collectors.toList());
        }

        // Filter by status
        if (status != null && !status.isEmpty()) {
            final int sf = Integer.parseInt(status);
            all = all.stream()
                    .filter(a -> a.getStatus() != null && a.getStatus() == sf)
                    .collect(Collectors.toList());
        }

        // Filter by date range
        String start = startTime != null ? startTime : beginTime;
        String end = endTime;
        if (start != null && end != null) {
            try {
                LocalDateTime begin = LocalDateTime.parse(start.replace(" ", "T"));
                LocalDateTime endDt = LocalDateTime.parse(end.replace(" ", "T"));
                all = all.stream()
                        .filter(a -> a.getCreateTime() != null
                                && !a.getCreateTime().isBefore(begin)
                                && !a.getCreateTime().isAfter(endDt))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }

        // Sort newest first
        all.sort((a, b) -> {
            if (a.getCreateTime() == null) return 1;
            if (b.getCreateTime() == null) return -1;
            return b.getCreateTime().compareTo(a.getCreateTime());
        });

        int total = all.size();
        int s = (pageNum - 1) * pageSize;
        int e = Math.min(s + pageSize, total);
        List<Alert> page = s < total ? all.subList(s, e) : new ArrayList<>();

        List<Map<String, Object>> records = page.stream().map(this::toMap).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        data.put("rows", records);
        data.put("totalCount", total);
        data.put("total", total);
        return ApiResponse.success(data);
    }

    @GetMapping("/api/alarm/record/{id}")
    public ApiResponse<Map<String, Object>> getAlarmRecord(@PathVariable Long id) {
        return alertRepository.findById(id)
                .map(a -> ApiResponse.success(toMap(a)))
                .orElse(ApiResponse.error(404, "Record not found"));
    }

    @PutMapping("/api/alarm/record/{id}")
    public ApiResponse<String> updateAlarmRecordById(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        alertRepository.findById(id).ifPresent(alert -> {
            if (body.containsKey("status")) {
                alert.setStatus(Integer.parseInt(body.get("status").toString()));
            }
            if (body.containsKey("remarks")) {
                alert.setRemarks(body.get("remarks") != null ? body.get("remarks").toString() : null);
            }
            alertRepository.save(alert);
        });
        return ApiResponse.success("Updated successfully");
    }

    @PutMapping("/api/alarm/record")
    public ApiResponse<String> updateAlarmRecord(@RequestBody Map<String, Object> body) {
        if (body.containsKey("id")) {
            Long id = Long.parseLong(body.get("id").toString());
            alertRepository.findById(id).ifPresent(alert -> {
                if (body.containsKey("status")) {
                    alert.setStatus(Integer.parseInt(body.get("status").toString()));
                }
                if (body.containsKey("remarks")) {
                    alert.setRemarks(body.get("remarks") != null ? body.get("remarks").toString() : null);
                }
                alertRepository.save(alert);
            });
        }
        return ApiResponse.success("Updated successfully");
    }

    @DeleteMapping("/api/alarm/record/{id}")
    public ApiResponse<String> deleteAlarmRecord(@PathVariable Long id) {
        alertRepository.deleteById(id);
        return ApiResponse.success("Deleted successfully");
    }

    @GetMapping("/api/alarm/record/export")
    public void exportAlarmRecords(
            @RequestParam(required = false) String eventTypeId,
            @RequestParam(required = false) String equipmentName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            jakarta.servlet.http.HttpServletResponse response) throws Exception {

        List<Alert> all = alertRepository.findAll().stream()
                .filter(a -> VIOLATION_TYPES.contains(a.getViolationType()))
                .collect(Collectors.toList());

        if (eventTypeId != null && !eventTypeId.isEmpty())
            all = all.stream().filter(a -> eventTypeId.equals(a.getViolationType())).collect(Collectors.toList());
        if (equipmentName != null && !equipmentName.isEmpty())
            all = all.stream().filter(a -> a.getDeviceName() != null && a.getDeviceName().contains(equipmentName)).collect(Collectors.toList());
        if (status != null && !status.isEmpty())
            all = all.stream().filter(a -> a.getStatus() != null && a.getStatus() == Integer.parseInt(status)).collect(Collectors.toList());

        all.sort((a, b) -> b.getCreateTime() != null && a.getCreateTime() != null ? b.getCreateTime().compareTo(a.getCreateTime()) : 0);

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Violation Type,Device Name,Confidence,Status,Create Time\n");
        for (Alert a : all) {
            csv.append(a.getId()).append(",")
               .append(a.getViolationType()).append(",")
               .append(a.getDeviceName() != null ? a.getDeviceName() : "").append(",")
               .append(a.getConfidence() != null ? a.getConfidence() : "").append(",")
               .append(a.getStatus() == 0 ? "Untreated" : a.getStatus() == 1 ? "Processed" : "Rejected").append(",")
               .append(a.getCreateTime() != null ? a.getCreateTime() : "").append("\n");
        }

        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=alarm_records.csv");
        response.getWriter().write(csv.toString());
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

    private Map<String, Object> toMap(Alert a) {
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
        r.put("remarks", a.getRemarks());
        r.put("createTime", a.getCreateTime() != null ? a.getCreateTime().toString() : null);
        r.put("sendTime", a.getCreateTime() != null ? a.getCreateTime().toString() : null);
        return r;
    }
}
