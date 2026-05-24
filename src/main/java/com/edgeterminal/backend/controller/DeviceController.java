package com.edgeterminal.backend.controller;

import com.edgeterminal.backend.dto.ApiResponse;
import com.edgeterminal.backend.entity.Device;
import com.edgeterminal.backend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;

    private static final String AI_SERVER = "opencode@192.168.100.69";
    private static final String ADD_CAMERA_SCRIPT = "/home/opencode/add_camera.py";

    @GetMapping
    public ApiResponse<Map<String, Object>> listDevice(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String deviceName) {
        List<Device> devices = deviceName != null
                ? deviceRepository.findByDeviceNameContaining(deviceName)
                : deviceRepository.findAll();
        Map<String, Object> data = new HashMap<>();
        data.put("records", devices);
        data.put("rows", devices);
        data.put("totalCount", devices.size());
        data.put("total", devices.size());
        return ApiResponse.success(data);
    }

    @GetMapping("/queryAll")
    public ApiResponse<List<Device>> listDeviceAll() {
        return ApiResponse.success(deviceRepository.findAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Device> getDevice(@PathVariable Long id) {
        return deviceRepository.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Device not found"));
    }

    @PostMapping
    public ApiResponse<Device> addDevice(@RequestBody Device device) {
        if (device.getStatus() == null) device.setStatus("online");
        Device saved = deviceRepository.save(device);

        // Create XML task on AI server if RTSP URL provided
        if (saved.getRtspMain() != null && !saved.getRtspMain().isEmpty()) {
            createAiTask(saved);
        }

        return ApiResponse.success(saved);
    }

    @PutMapping
    public ApiResponse<Device> updateDevice(@RequestBody Device device) {
        Device saved = deviceRepository.save(device);
        return ApiResponse.success(saved);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteDevice(@PathVariable Long id) {
        deviceRepository.deleteById(id);
        return ApiResponse.success("Device deleted successfully");
    }

    @GetMapping("/taskStatus/audioStatus")
    public ApiResponse<String> audioStatus(@RequestParam String status) {
        return ApiResponse.success("Audio status updated");
    }

    @GetMapping("/taskStatus/getAudioStatus")
    public ApiResponse<Map<String, Object>> getAudioStatus() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", 10);
        return ApiResponse.success(data);
    }

    private void createAiTask(Device device) {
        try {
            String cmd = String.format(
                "ssh %s python3 %s '%s' '%s' '%s'",
                AI_SERVER,
                ADD_CAMERA_SCRIPT,
                device.getDeviceName(),
                device.getRtspMain(),
                device.getId().toString()
            );
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.waitFor();
            System.out.println("AI task created for device: " + device.getDeviceName());
        } catch (Exception e) {
            System.err.println("Failed to create AI task: " + e.getMessage());
        }
    }
}
