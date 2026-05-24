package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PushConfigController {

    private final SystemConfigService configService;

    @GetMapping("/api/http/push")
    public ApiResponse<Map<String, Object>> listHttp(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(configService.listPush("http", pageNum, pageSize));
    }

    @GetMapping("/api/http/push/{id}")
    public ApiResponse<Map<String, Object>> getHttp(@PathVariable Long id) {
        return ApiResponse.success(configService.getPush("http", id));
    }

    @PostMapping("/api/http/push")
    public ApiResponse<Map<String, Object>> addHttp(@RequestBody Map<String, Object> body) {
        return ApiResponse.success(configService.addPush("http", body));
    }

    @PutMapping("/api/http/push")
    public ApiResponse<String> updateHttp(@RequestBody Map<String, Object> body) {
        configService.updatePush("http", body);
        return ApiResponse.success("Updated");
    }

    @DeleteMapping("/api/http/push/{id}")
    public ApiResponse<String> deleteHttp(@PathVariable Long id) {
        configService.deletePush("http", id);
        return ApiResponse.success("Deleted");
    }

    @GetMapping("/api/phone/push")
    public ApiResponse<Map<String, Object>> listPhone(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(configService.listPush("phone", pageNum, pageSize));
    }

    @GetMapping("/api/phone/push/{id}")
    public ApiResponse<Map<String, Object>> getPhone(@PathVariable Long id) {
        return ApiResponse.success(configService.getPush("phone", id));
    }

    @PostMapping("/api/phone/push")
    public ApiResponse<Map<String, Object>> addPhone(@RequestBody Map<String, Object> body) {
        return ApiResponse.success(configService.addPush("phone", body));
    }

    @PutMapping("/api/phone/push")
    public ApiResponse<String> updatePhone(@RequestBody Map<String, Object> body) {
        configService.updatePush("phone", body);
        return ApiResponse.success("Updated");
    }

    @DeleteMapping("/api/phone/push/{ids}")
    public ApiResponse<String> deletePhone(@PathVariable String ids) {
        for (String part : ids.split(",")) {
            configService.deletePush("phone", Long.parseLong(part.trim()));
        }
        return ApiResponse.success("Deleted");
    }

    @GetMapping("/api/email/push")
    public ApiResponse<Map<String, Object>> listEmail(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(configService.listPush("email", pageNum, pageSize));
    }

    @GetMapping("/api/email/push/{id}")
    public ApiResponse<Map<String, Object>> getEmail(@PathVariable Long id) {
        return ApiResponse.success(configService.getPush("email", id));
    }

    @PostMapping("/api/email/push")
    public ApiResponse<Map<String, Object>> addEmail(@RequestBody Map<String, Object> body) {
        return ApiResponse.success(configService.addPush("email", body));
    }

    @PutMapping("/api/email/push")
    public ApiResponse<String> updateEmail(@RequestBody Map<String, Object> body) {
        configService.updatePush("email", body);
        return ApiResponse.success("Updated");
    }

    @DeleteMapping("/api/email/push/{ids}")
    public ApiResponse<String> deleteEmail(@PathVariable String ids) {
        for (String part : ids.split(",")) {
            configService.deletePush("email", Long.parseLong(part.trim()));
        }
        return ApiResponse.success("Deleted");
    }

    @GetMapping("/api/mqtt/push/{id}")
    public ApiResponse<Map<String, Object>> getMqtt(@PathVariable Long id) {
        return ApiResponse.success(configService.getMqtt(id));
    }

    @PutMapping("/api/mqtt/push")
    public ApiResponse<String> updateMqtt(@RequestBody Map<String, Object> body) {
        configService.updateMqtt(body);
        return ApiResponse.success("Updated");
    }

    @GetMapping("/api/audio/config")
    public ApiResponse<Map<String, Object>> listAudio(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(configService.listPush("audio", pageNum, pageSize));
    }

    @GetMapping("/api/audio/config/{id}")
    public ApiResponse<Map<String, Object>> getAudio(@PathVariable Long id) {
        return ApiResponse.success(configService.getPush("audio", id));
    }

    @PostMapping("/api/audio/config")
    public ApiResponse<Map<String, Object>> addAudio(@RequestBody Map<String, Object> body) {
        return ApiResponse.success(configService.addPush("audio", body));
    }

    @PutMapping("/api/audio/config")
    public ApiResponse<String> updateAudio(@RequestBody Map<String, Object> body) {
        configService.updatePush("audio", body);
        return ApiResponse.success("Updated");
    }

    @DeleteMapping("/api/audio/config/{id}")
    public ApiResponse<String> deleteAudio(@PathVariable Long id) {
        configService.deletePush("audio", id);
        return ApiResponse.success("Deleted");
    }

    @GetMapping("/api/push/service/config/{id}")
    public ApiResponse<Map<String, Object>> getPublicAccount(@PathVariable Long id) {
        return ApiResponse.success(configService.getPublicAccount(id));
    }

    @PutMapping("/api/push/service/config")
    public ApiResponse<String> updatePublicAccount(@RequestBody Map<String, Object> body) {
        configService.updatePublicAccount(body);
        return ApiResponse.success("Updated");
    }
}
