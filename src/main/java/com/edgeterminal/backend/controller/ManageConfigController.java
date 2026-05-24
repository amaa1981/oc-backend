package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/manageConfig")
@RequiredArgsConstructor
public class ManageConfigController {

    private final SystemConfigService configService;

    @GetMapping("/analysis/getProcessInfo")
    public ApiResponse<Map<String, Object>> analysisProcessInfo() {
        return ApiResponse.success(configService.processListResponse());
    }

    @GetMapping("/analysis/restartProcess")
    public ApiResponse<Map<String, Object>> restartAnalysis() {
        return ApiResponse.success(configService.processActionResponse());
    }

    @GetMapping("/media/getProcessInfo")
    public ApiResponse<Map<String, Object>> mediaProcessInfo() {
        return ApiResponse.success(configService.processListResponse());
    }

    @GetMapping("/media/restartProcess")
    public ApiResponse<Map<String, Object>> restartMedia() {
        return ApiResponse.success(configService.processActionResponse());
    }
}
