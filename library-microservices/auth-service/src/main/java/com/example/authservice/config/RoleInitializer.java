package com.example.authservice.config;

import com.example.authservice.domain.entity.RoleEntity;
import com.example.authservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Value("${app.roles}")
    private String rolesConfig;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        String[] roles = rolesConfig.split(",");

        for (String roleName : roles) {
            roleName = roleName.trim();
            if (!roleRepository.findByName(roleName).isPresent()) {
                RoleEntity role = new RoleEntity();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}
