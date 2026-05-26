package com.edgeterminal.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ai_alert")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "device_name")
    private String deviceName;

    // Violation: no_glove, no_mask, no_hairnet
    @Column(name = "violation_type")
    private String violationType;

    @Column(name = "confidence")
    private Float confidence;

    // Base64 snapshot image
    @Column(name = "image_data", columnDefinition = "LONGTEXT")
    private String imageData;

    // 0 = unprocessed, 1 = processed
    @Column(name = "status")
    private Integer status = 0;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}