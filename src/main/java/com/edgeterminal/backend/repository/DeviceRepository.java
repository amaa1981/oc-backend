package com.edgeterminal.backend.repository;

import com.edgeterminal.backend.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByDeviceNameContaining(String deviceName);
}
