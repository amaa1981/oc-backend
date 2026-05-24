package com.edgeterminal.backend.config;

import com.edgeterminal.backend.entity.DeviceArea;
import com.edgeterminal.backend.entity.User;
import com.edgeterminal.backend.repository.DeviceAreaRepository;
import com.edgeterminal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DeviceAreaRepository deviceAreaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create default admin user if no users exist
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNickname("Administrator");
            admin.setEmail("admin@edgeterminal.com");
            admin.setStatus(0);
            admin.setDeleted(0);

            userRepository.save(admin);
            log.info("Default admin user created — username: admin, password: admin123");
            log.info("Please change the default password after first login!");
        }

        if (deviceAreaRepository.count() == 0) {
            seedArea("1", "Main Kitchen", 1);
            seedArea("2", "Prep Area", 2);
            seedArea("3", "Storage Room", 3);
            seedArea("4", "Entrance", 4);
            log.info("Default device areas seeded");
        }
    }

    private void seedArea(String value, String label, int sort) {
        DeviceArea area = new DeviceArea();
        area.setDictValue(value);
        area.setDictLabel(label);
        area.setDictSort(sort);
        area.setDictType("v1_device_area");
        area.setStatus("0");
        deviceAreaRepository.save(area);
    }
}
