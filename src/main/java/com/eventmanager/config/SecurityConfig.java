package com.eventmanager.config;

import com.eventmanager.entity.User;
import com.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final UserRepository userRepository;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/events/**").authenticated()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});
        
        return http.build();
    }
    
    @Bean
    public CommandLineRunner initAdminUser() {
        return args -> {
            // Create admin user if not exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@eventmanager.com")
                        .password(passwordEncoder().encode("admin123"))
                        .role(User.UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println("Admin user created: admin/admin123");
            }
        };
    }
}