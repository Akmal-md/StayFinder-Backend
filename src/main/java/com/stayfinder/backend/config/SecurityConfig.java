package com.stayfinder.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import com.stayfinder.backend.security.JwtAuthenticationFilter;

@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/register", "/login").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/properties", "/properties/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/my-properties").hasRole("OWNER")
                        .requestMatchers(HttpMethod.POST, "/properties").hasRole("OWNER")
                        .requestMatchers(HttpMethod.POST, "/upload").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/properties/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/properties/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/inquiries/owner").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/inquiries/tenant").hasRole("TENANT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
        
    }
}
