package com.project.clinic.configurations;
import com.project.clinic.models.User;
import com.project.clinic.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initUsers(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
            Optional<User> adminUser = userService.findByUsername("admin");
            if (adminUser.isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin");
                admin.setRoles("ROLE_ADMIN");
                userService.saveUser(admin);
            }

            Optional<User> receptionUser = userService.findByUsername("user");
            if (receptionUser.isEmpty()) {
                User reception = new User();
                reception.setUsername("user");
                reception.setPassword("user");
                reception.setRoles("ROLE_USER");
                userService.saveUser(reception);
            }
        };
    }
}
