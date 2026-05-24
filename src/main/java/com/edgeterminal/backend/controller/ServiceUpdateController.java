package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/service/update")
@RequiredArgsConstructor
public class ServiceUpdateController {

    private final SystemConfigService configService;

    @GetMapping
    public ApiResponse<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(configService.listServicePackages(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> get(@PathVariable Long id) {
        return ApiResponse.success(configService.getServicePackage(id));
    }

    @GetMapping("/new")
    public ApiResponse<Map<String, Object>> getLatest() {
        return ApiResponse.success(new HashMap<>());
    }

    @PostMapping
    public ApiResponse<String> add(@RequestBody Map<String, Object> body) {
        return ApiResponse.success("Added");
    }

    @PutMapping
    public ApiResponse<String> update(@RequestBody Map<String, Object> body) {
        return ApiResponse.success("Updated");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        return ApiResponse.success("Deleted");
    }

    @PostMapping("/updateJar")
    public ApiResponse<String> updateJar() {
        return ApiResponse.success("JAR upload received");
    }

    @PostMapping("/updateWeb")
    public ApiResponse<String> updateWeb() {
        return ApiResponse.success("Web package upload received");
    }
}
