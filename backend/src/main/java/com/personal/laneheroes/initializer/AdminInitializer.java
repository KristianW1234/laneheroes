package com.personal.laneheroes.initializer;

import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.enums.Role;
import com.personal.laneheroes.repositories.UserRepository;
import com.personal.laneheroes.utilities.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("!test")
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public AdminInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlMode;

    @Override
    public void run(String... args) throws Exception {
        if (!"none".equalsIgnoreCase(ddlMode) && userRepository.findByUserName("admin").isEmpty()) {
            User admin = new User();
            admin.setUserName("admin");
            admin.setUserPassword(PasswordUtil.encode("admin123")); // use your encoder
            admin.setUserRole(Role.ADMIN);
            admin.setIsActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
        }
    }
}