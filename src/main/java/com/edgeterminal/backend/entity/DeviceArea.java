package com.edgeterminal.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "device_area")
public class DeviceArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictCode;

    @Column(nullable = false)
    private String dictLabel;

    @Column(nullable = false, unique = true)
    private String dictValue;

    private Integer dictSort = 0;

    @Column(nullable = false)
    private String dictType = "v1_device_area";

    private String status = "0";
}
