package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.LoginLog;
import com.edgeterminal.backend.repository.LoginLogRepository;
import com.edgeterminal.backend.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final SystemConfigService configService;
    private final LoginLogRepository loginLogRepository;

    @GetMapping("/operlog/list")
    public ApiResponse<Map<String, Object>> operLogList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(configService.listLogsAsRows(pageNum, pageSize));
    }

    @DeleteMapping("/operlog/{operId}")
    public ApiResponse<String> deleteOperLog(@PathVariable String operId) {
        return ApiResponse.success("Deleted");
    }

    @DeleteMapping("/operlog/clean")
    public ApiResponse<String> cleanOperLog() {
        return ApiResponse.success("Cleared");
    }

    @GetMapping("/logininfor/list")
    public ApiResponse<Map<String, Object>> loginLogList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String userName) {
        List<LoginLog> logs = userName != null && !userName.isEmpty()
            ? loginLogRepository.findByUserNameContainingOrderByLoginTimeDesc(userName)
            : loginLogRepository.findAllByOrderByLoginTimeDesc();
        int total = logs.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<LoginLog> page = start < total ? logs.subList(start, end) : new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("rows", page);
        data.put("total", total);
        return ApiResponse.success(data);
    }

    @DeleteMapping("/logininfor/{infoId}")
    public ApiResponse<String> deleteLoginLogById(@PathVariable Long infoId) {
        loginLogRepository.deleteById(infoId);
        return ApiResponse.success("Deleted");
    }



    @GetMapping("/logininfor/unlock/{userName}")
    public ApiResponse<String> unlock(@PathVariable String userName) {
        return ApiResponse.success("Unlocked");
    }

    @DeleteMapping("/logininfor/clean")
    public ApiResponse<String> cleanLoginLog() {
        return ApiResponse.success("Cleared");
    }

    @GetMapping("/server")
    public ApiResponse<Map<String, Object>> server() {
        return ApiResponse.success(configService.serverInfo());
    }
}
