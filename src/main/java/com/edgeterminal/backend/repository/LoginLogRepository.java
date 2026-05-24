package com.edgeterminal.backend.repository;

import com.edgeterminal.backend.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    List<LoginLog> findByUserNameContainingOrderByLoginTimeDesc(String userName);
    List<LoginLog> findAllByOrderByLoginTimeDesc();
}
