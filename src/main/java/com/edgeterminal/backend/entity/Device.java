package com.edgeterminal.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceName;
    private String deviceId;
    private String deviceType;
    private String factoryType;
    private String protocolType;
    private String installationArea;
    private String installationLocation;
    private String deviceIp;
    private String devicePort;
    private String userName;
    private String password;
    private String deviceChannel;
    private String rtspMain;
    private String rtspSub;
    private String coordinate;
    private String status;
    private String name;
    private String location;

    @CreationTimestamp
    private LocalDateTime createTime;
}
