package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PeopleController {

    @GetMapping
    public ApiResponse<Map<String, Object>> listPeople(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = new HashMap<>();
        data.put("records", new ArrayList<>());
        data.put("totalCount", 0);
        data.put("total", 0);
        return ApiResponse.success(data);
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getPeople(@PathVariable Long id) {
        return ApiResponse.success(new HashMap<>());
    }

    @PostMapping
    public ApiResponse<String> addPeople(@RequestBody Map<String, Object> people) {
        return ApiResponse.success("Added successfully");
    }

    @PutMapping
    public ApiResponse<String> updatePeople(@RequestBody Map<String, Object> people) {
        return ApiResponse.success("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePeople(@PathVariable Long id) {
        return ApiResponse.success("Deleted successfully");
    }
}