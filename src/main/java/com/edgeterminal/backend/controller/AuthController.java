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

        // Alarm Map
        routers.add(buildRoute("AlarmMap", "/alarmMap", "Layout", "Alarm map", "international",
                List.of(buildChild("AlarmMapIndex", "index", "system/map/index", "Alarm map", "international"))));

        // Monitoring Center
        routers.add(buildRoute("Monitor", "/monitor", "Layout", "Monitoring Center", "monitor",
                List.of(buildChild("MonitorIndex", "index", "monitor/index", "Monitoring Center", "monitor"))));

        // Alarm Management
        routers.add(buildRoute("alarmmanger", "/alarmmanger", "Layout", "Alarms", "bell",
                List.of(
                        buildChild("AlarmRecord", "record", "system/record/index", "Alarm Log", "form"),
                        buildChild("AlarmPeople", "people", "system/people/index", "Facial Information", "people"),
                        buildChild("AlarmDevice", "device", "system/alarm/index", "Device Information", "phone"),
                        buildChild("AlarmArea", "area", "system/area/index", "Equipment area", "tree-table")
                )));

        // System Configuration
        routers.add(buildRoute("backgroundManage", "/backgroundManage", "Layout", "Configuration", "system",
                List.of(
                        buildChild("BackgroundManage", "background", "system/backgroundManage/index", "Background", "component"),
                        buildChild("LoginLog", "logininfor", "monitor/logininfor/index", "Login Log", "logininfor"),
                        buildChild("OperLog", "operlog", "monitor/operlog/index", "Operation Log", "form"),
                        buildChild("ServerMonitor", "server", "monitor/server/index", "Server Monitor", "server")
                )));

        // System Management
        routers.add(buildRoute("System", "/system", "Layout", "System", "peoples",
                List.of(
                        buildChild("User", "user", "system/user/index", "Users", "user"),
                        buildChild("Role", "role", "system/role/index", "Roles", "peoples"),
                        buildChild("Menu", "menu", "system/menu/index", "Menus", "tree-table"),
                        buildChild("Dept", "dept", "system/dept/index", "Departments", "tree"),
                        buildChild("Post", "post", "system/post/index", "Positions", "post"),
                        buildChild("Dict", "dict", "system/dict/index", "Dictionary", "dict")
                )));

        return ApiResponse.success(routers);
    }

    private Map<String, Object> buildRoute(String name, String path, String component,
                                            String title, String icon,
                                            List<Map<String, Object>> children) {
        Map<String, Object> route = new HashMap<>();
        route.put("name", name);
        route.put("path", path);
        route.put("hidden", false);
        route.put("component", component);
        route.put("meta", buildMeta(title, icon));
        route.put("children", children);
        return route;
    }

    private Map<String, Object> buildChild(String name, String path, String component,
                                            String title, String icon) {
        Map<String, Object> child = new HashMap<>();
        child.put("name", name);
        child.put("path", path);
        child.put("hidden", false);
        child.put("component", component);
        child.put("meta", buildMeta(title, icon));
        return child;
    }

    private Map<String, Object> buildMeta(String title, String icon) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("title", title);
        meta.put("icon", icon);
        meta.put("noCache", false);
        return meta;
    }

    @GetMapping("/system/dict/data/type/{dictType}")
    public ApiResponse<List<Map<String, Object>>> getDictData(@PathVariable String dictType) {
        return ApiResponse.success(new ArrayList<>());
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