package com.edgeterminal.backend.service;

import com.edgeterminal.backend.entity.Alert;
import com.edgeterminal.backend.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Save alert and push via WebSocket
    public Alert createAlert(Alert alert) {
        Alert saved = alertRepository.save(alert);

        // Push to dashboard via WebSocket
        Map<String, Object> message = new HashMap<>();
        message.put("id", saved.getId());
        message.put("deviceId", saved.getDeviceId());
        message.put("deviceName", saved.getDeviceName());
        message.put("violationType", saved.getViolationType());
        message.put("confidence", saved.getConfidence());
        message.put("imageData", saved.getImageData());
        message.put("createTime", saved.getCreateTime().toString());

        messagingTemplate.convertAndSend("/topic/alerts", message);

        return saved;
    }

    // Get recent alerts
    public List<Alert> getRecentAlerts() {
        return alertRepository.findTop10ByOrderByCreateTimeDesc();
    }

    // Get unprocessed alerts
    public List<Alert> getUnprocessedAlerts() {
        return alertRepository.findByStatusOrderByCreateTimeDesc(0);
    }

    // Mark alert as processed
    public Alert processAlert(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setStatus(1);
        return alertRepository.save(alert);
    }

    // Get alert stats
    public Map<String, Long> getAlertStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", alertRepository.count());
        stats.put("unprocessed", alertRepository.countByStatus(0));
        stats.put("processed", alertRepository.countByStatus(1));
        return stats;
    }
}