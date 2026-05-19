package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class RoleController {

    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> listRoles(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("rows", new ArrayList<>());
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/{roleId}")
    public ApiResponse<Map<String, Object>> getRole(@PathVariable Long roleId) {
        return ApiResponse.success(new HashMap<>());
    }

    @PostMapping
    public ApiResponse<String> addRole(@RequestBody Map<String, Object> role) {
        return ApiResponse.success("Role added successfully");
    }

    @PutMapping
    public ApiResponse<String> updateRole(@RequestBody Map<String, Object> role) {
        return ApiResponse.success("Role updated successfully");
    }

    @PutMapping("/dataScope")
    public ApiResponse<String> dataScope(@RequestBody Map<String, Object> role) {
        return ApiResponse.success("Data scope updated successfully");
    }

    @PutMapping("/changeStatus")
    public ApiResponse<String> changeStatus(@RequestBody Map<String, Object> role) {
        return ApiResponse.success("Status changed successfully");
    }

    @DeleteMapping("/{roleId}")
    public ApiResponse<String> deleteRole(@PathVariable Long roleId) {
        return ApiResponse.success("Role deleted successfully");
    }

    @GetMapping("/authUser/allocatedList")
    public ApiResponse<Map<String, Object>> allocatedList() {
        Map<String, Object> data = new HashMap<>();
        data.put("rows", new ArrayList<>());
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/authUser/unallocatedList")
    public ApiResponse<Map<String, Object>> unallocatedList() {
        Map<String, Object> data = new HashMap<>();
        data.put("rows", new ArrayList<>());
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @PutMapping("/authUser/cancel")
    public ApiResponse<String> cancelAuth() {
        return ApiResponse.success("Auth cancelled successfully");
    }

    @PutMapping("/authUser/cancelAll")
    public ApiResponse<String> cancelAllAuth() {
        return ApiResponse.success("All auth cancelled successfully");
    }

    @PutMapping("/authUser/selectAll")
    public ApiResponse<String> selectAllAuth() {
        return ApiResponse.success("All auth selected successfully");
    }

    @GetMapping("/deptTree/{roleId}")
    public ApiResponse<List<Object>> deptTree(@PathVariable Long roleId) {
        return ApiResponse.success(new ArrayList<>());
    }
}