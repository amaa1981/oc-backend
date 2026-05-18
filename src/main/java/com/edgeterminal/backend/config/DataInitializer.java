package com.edgeterminal.backend.config;

import com.edgeterminal.backend.entity.User;
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
    }
}
