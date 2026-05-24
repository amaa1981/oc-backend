package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SysConfigController {

    private final SystemConfigService configService;

    @GetMapping("/api/sysConfig/{id}")
    public ApiResponse<Map<String, Object>> getSysConfig(@PathVariable Long id) {
        return ApiResponse.success(configService.getSysConfig(id));
    }

    @PutMapping("/api/sysConfig")
    public ApiResponse<String> updateSysConfig(@RequestBody Map<String, Object> config) {
        configService.updateSysConfig(config);
        return ApiResponse.success("Config updated successfully");
    }

    @GetMapping("/api/sysConfig/getLocale")
    public ApiResponse<String> getLocale() {
        return ApiResponse.success(configService.getLocale());
    }

    @GetMapping("/api/sysConfig/setLocale")
    public ApiResponse<String> setLocale(@RequestParam String locale) {
        configService.setLocale(locale);
        return ApiResponse.success("Locale updated");
    }

    @GetMapping("/api/sysConfig/java")
    public ApiResponse<Map<String, Object>> javaConfig() {
        return ApiResponse.success(configService.javaConfig());
    }
}
