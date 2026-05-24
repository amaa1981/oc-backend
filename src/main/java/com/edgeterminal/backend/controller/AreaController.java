package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.DeviceArea;
import com.edgeterminal.backend.repository.DeviceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/area")
@RequiredArgsConstructor
public class AreaController {

    private static final String DEVICE_AREA_TYPE = "v1_device_area";

    private final DeviceAreaRepository deviceAreaRepository;

    @GetMapping
    public ApiResponse<Map<String, Object>> listArea(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<DeviceArea> all = deviceAreaRepository.findByDictTypeOrderByDictSortAsc(DEVICE_AREA_TYPE);
        int from = Math.max(0, (pageNum - 1) * pageSize);
        int to = Math.min(all.size(), from + pageSize);
        List<DeviceArea> page = from >= all.size() ? List.of() : all.subList(from, to);

        Map<String, Object> data = new HashMap<>();
        data.put("rows", page);
        data.put("total", all.size());
        return ApiResponse.success(data);
    }

    @GetMapping("/{id}")
    public ApiResponse<DeviceArea> getArea(@PathVariable Long id) {
        return deviceAreaRepository.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Area not found"));
    }

    @PostMapping
    public ApiResponse<DeviceArea> addArea(@RequestBody DeviceArea area) {
        area.setDictCode(null);
        area.setDictType(DEVICE_AREA_TYPE);
        if (area.getStatus() == null) {
            area.setStatus("0");
        }
        if (area.getDictValue() == null || area.getDictValue().isBlank()) {
            area.setDictValue(UUID.randomUUID().toString());
        }
        if (area.getDictSort() == null) {
            area.setDictSort(0);
        }
        return ApiResponse.success(deviceAreaRepository.save(area));
    }

    @PutMapping
    public ApiResponse<DeviceArea> updateArea(@RequestBody DeviceArea area) {
        if (area.getDictCode() == null) {
            return ApiResponse.error(400, "Area id is required");
        }
        return deviceAreaRepository.findById(area.getDictCode())
                .map(existing -> {
                    existing.setDictLabel(area.getDictLabel());
                    existing.setDictSort(area.getDictSort() != null ? area.getDictSort() : existing.getDictSort());
                    if (area.getDictValue() != null && !area.getDictValue().isBlank()) {
                        existing.setDictValue(area.getDictValue());
                    }
                    if (area.getStatus() != null) {
                        existing.setStatus(area.getStatus());
                    }
                    return ApiResponse.success(deviceAreaRepository.save(existing));
                })
                .orElse(ApiResponse.error(404, "Area not found"));
    }

    @DeleteMapping("/{ids}")
    public ApiResponse<String> deleteArea(@PathVariable String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
        deviceAreaRepository.deleteAllById(idList);
        return ApiResponse.success("Area deleted successfully");
    }
}
