package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class CommonController {

    @PostMapping("/api/common/upload")
    public ApiResponse<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
            String dataUrl = "data:" + file.getContentType() + ";base64," + base64;
            Map<String, Object> result = new HashMap<>();
            result.put("url", dataUrl);
            result.put("fileName", dataUrl);
            result.put("newFileName", file.getOriginalFilename());
            result.put("originalFilename", file.getOriginalFilename());
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "Upload failed: " + e.getMessage());
        }
    }
}
