package com.edgeterminal.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "people")
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String sex;
    private String type;
    private String phone;
    private String idCard;
    private String department;
    @Column(columnDefinition = "LONGTEXT")
    private String faceImage;
    private String status;
    @CreationTimestamp
    private LocalDateTime createTime;
}
