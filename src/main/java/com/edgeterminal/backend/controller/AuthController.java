package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.service.AuthService;
import com.edgeterminal.backend.util.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = authService.login(request.getUsername(), request.getPassword());
            return ApiResponse.success(result);
        } catch (RuntimeException e) {
            return ApiResponse.error(500, e.getMessage());
        }
    }

    @PostMapping("/auth/logout")
    public ApiResponse<String> logout() {
        return ApiResponse.success("Logged out successfully");
    }

    @GetMapping("/auth/info")
    public ApiResponse<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(token);
            Map<String, Object> userInfo = authService.getUserInfo(username);
            return ApiResponse.success(userInfo);
        } catch (Exception e) {
            return ApiResponse.error(401, "Invalid or expired token");
        }
    }

    @GetMapping("/auth/captchaImage")
    public ApiResponse<Map<String, Object>> captchaImage() {
        Map<String, Object> result = new HashMap<>();
        result.put("captchaEnabled", false);
        result.put("uuid", "");
        result.put("img", "");
        return ApiResponse.success(result);
    }

    @GetMapping("/getRouters")
    public ApiResponse<List<Map<String, Object>>> getRouters() {
        List<Map<String, Object>> routers = new ArrayList<>();

        Map<String, Object> home = new HashMap<>();
        home.put("name", "Data");
        home.put("path", "/data");
        home.put("hidden", false);
        home.put("component", "data/index");
        Map<String, Object> homeMeta = new HashMap<>();
        homeMeta.put("title", "Dashboard");
        homeMeta.put("icon", "dashboard");
        homeMeta.put("noCache", false);
        home.put("meta", homeMeta);
        routers.add(home);

        return ApiResponse.success(routers);
    }

    @GetMapping("/system/dict/data/type/{dictType}")
    public ApiResponse<List<Map<String, Object>>> getDictData(@PathVariable String dictType) {
        List<Map<String, Object>> dictData = new ArrayList<>();
        return ApiResponse.success(dictData);
    }

    @Data
    static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        private String code;
        private String uuid;
    }
}