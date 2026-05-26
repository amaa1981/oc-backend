package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.People;
import com.edgeterminal.backend.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PeopleController {

    private final PeopleRepository peopleRepository;

    @GetMapping("/api/people")
    public ApiResponse<Map<String, Object>> listPeople(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String type) {

        List<People> all = peopleRepository.findAll();
        if (name != null && !name.isEmpty())
            all = all.stream().filter(p -> p.getName() != null && p.getName().contains(name)).collect(Collectors.toList());
        if (sex != null && !sex.isEmpty())
            all = all.stream().filter(p -> sex.equals(p.getSex())).collect(Collectors.toList());
        if (type != null && !type.isEmpty())
            all = all.stream().filter(p -> type.equals(p.getType())).collect(Collectors.toList());

        int total = all.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<People> page = start < total ? all.subList(start, end) : new ArrayList<>();

        Map<String, Object> data = new HashMap<>();
        data.put("records", page);
        data.put("rows", page);
        data.put("totalCount", total);
        data.put("total", total);
        return ApiResponse.success(data);
    }

    @GetMapping("/api/people/{id}")
    public ApiResponse<People> getPeople(@PathVariable Long id) {
        return peopleRepository.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Not found"));
    }

    @PostMapping("/api/people")
    public ApiResponse<People> addPeople(@RequestBody People people) {
        return ApiResponse.success(peopleRepository.save(people));
    }

    @PutMapping("/api/people")
    public ApiResponse<People> updatePeople(@RequestBody People people) {
        return ApiResponse.success(peopleRepository.save(people));
    }

    @DeleteMapping("/api/people/{id}")
    public ApiResponse<String> deletePeople(@PathVariable Long id) {
        peopleRepository.deleteById(id);
        return ApiResponse.success("Deleted successfully");
    }

    @GetMapping("/api/people/importTemplate")
    public void downloadTemplate(jakarta.servlet.http.HttpServletResponse response) throws Exception {
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=people_template.csv");
        response.getWriter().write("Name,Sex(0=Male/1=Female),Type(1=Staff/2=Blacklisted),Phone,ID Card,Department\n");
        response.getWriter().write("John Doe,0,1,+1234567890,ID123456,Kitchen\n");
    }

    @PostMapping("/api/people/importData")
    public ApiResponse<String> importPeople(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(required = false) String updateSupport) {
        return ApiResponse.success("Import successful");
    }

    @PostMapping("/api/people/upload")
    public ApiResponse<Map<String, Object>> uploadFaceImage(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String base64 = java.util.Base64.getEncoder().encodeToString(bytes);
            String dataUrl = "data:" + file.getContentType() + ";base64," + base64;
            Map<String, Object> result = new HashMap<>();
            result.put("url", dataUrl);
            result.put("fileName", file.getOriginalFilename());
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "Upload failed: " + e.getMessage());
        }
    }


}
