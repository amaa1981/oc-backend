package com.edgeterminal.backend.repository;

import com.edgeterminal.backend.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByStatusOrderByCreateTimeDesc(Integer status);

    List<Alert> findByDeviceIdOrderByCreateTimeDesc(String deviceId);

    List<Alert> findTop10ByOrderByCreateTimeDesc();

    long countByStatus(Integer status);
}