package com.edgeterminal.backend.repository;

import com.edgeterminal.backend.entity.DeviceArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceAreaRepository extends JpaRepository<DeviceArea, Long> {

    List<DeviceArea> findByDictTypeOrderByDictSortAsc(String dictType);
}
