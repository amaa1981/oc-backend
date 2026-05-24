package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/network/config")
@RequiredArgsConstructor
public class NetworkConfigController {

    private final SystemConfigService configService;

    @GetMapping
    public ApiResponse<Map<String, Object>> getAll() {
        return ApiResponse.success(configService.getNetwork(1L));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> get(@PathVariable Long id) {
        return ApiResponse.success(configService.getNetwork(id));
    }

    @PutMapping
    public ApiResponse<String> update(@RequestBody Map<String, Object> body) {
        configService.updateNetwork(body);
        return ApiResponse.success("Network config updated");
    }

    @PostMapping("/reboot")
    public ApiResponse<String> reboot(@RequestBody(required = false) Map<String, Object> body) {
        return ApiResponse.success("Reboot scheduled");
    }
}
