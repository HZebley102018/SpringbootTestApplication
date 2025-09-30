package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Configuration
public class SuperUserConfig {

    // Create a superuser on application startup if it doesn't exist
    @Bean
    public CommandLineRunner createSuperUser(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findByUserName("superuser")
            .ifPresentOrElse(
                user -> System.out.println("Superuser already exists."),
                () -> {
                    User superUser = new User(
                        "Super",
                        "User",
                        "superuser",
                        "superuser@example.com",
                        passwordEncoder.encode("superpassword"),
                        "SUPERUSER"
                    );
                    userRepository.save(superUser);
                    System.out.println("Superuser created.");
                }
            );
        };
    }
}


