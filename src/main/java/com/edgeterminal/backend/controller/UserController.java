package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> listUsers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("rows", new ArrayList<>());
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/{userId}")
    public ApiResponse<Map<String, Object>> getUser(@PathVariable Long userId) {
        return ApiResponse.success(new HashMap<>());
    }

    @PostMapping
    public ApiResponse<String> addUser(@RequestBody Map<String, Object> user) {
        return ApiResponse.success("User added successfully");
    }

    @PutMapping
    public ApiResponse<String> updateUser(@RequestBody Map<String, Object> user) {
        return ApiResponse.success("User updated successfully");
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable Long userId) {
        return ApiResponse.success("User deleted successfully");
    }

    @PutMapping("/resetPwd")
    public ApiResponse<String> resetPwd(@RequestBody Map<String, Object> user) {
        return ApiResponse.success("Password reset successfully");
    }

    @PutMapping("/changeStatus")
    public ApiResponse<String> changeStatus(@RequestBody Map<String, Object> user) {
        return ApiResponse.success("Status changed successfully");
    }

    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> getProfile() {
        Map<String, Object> data = new HashMap<>();
        data.put("user", new HashMap<>());
        data.put("roleGroup", "");
        data.put("postGroup", "");
        return ApiResponse.success(data);
    }

    @PutMapping("/profile")
    public ApiResponse<String> updateProfile(@RequestBody Map<String, Object> user) {
        return ApiResponse.success("Profile updated successfully");
    }

    @PutMapping("/profile/updatePwd")
    public ApiResponse<String> updatePwd(@RequestBody Map<String, Object> user) {
        return ApiResponse.success("Password updated successfully");
    }

    @PostMapping("/profile/avatar")
    public ApiResponse<String> updateAvatar() {
        return ApiResponse.success("Avatar updated successfully");
    }

    @GetMapping("/authRole/{userId}")
    public ApiResponse<Map<String, Object>> getAuthRole(@PathVariable Long userId) {
        Map<String, Object> data = new HashMap<>();
        data.put("roles", new ArrayList<>());
        data.put("user", new HashMap<>());
        return ApiResponse.success(data);
    }

    @PutMapping("/authRole")
    public ApiResponse<String> updateAuthRole() {
        return ApiResponse.success("Role updated successfully");
    }

    @GetMapping("/deptTree")
    public ApiResponse<List<Object>> deptTree() {
        return ApiResponse.success(new ArrayList<>());
    }
}