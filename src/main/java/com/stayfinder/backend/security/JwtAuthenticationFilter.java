package com.stayfinder.backend.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stayfinder.backend.User;
import com.stayfinder.backend.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService , UserRepository userRepository) {
         this.jwtService = jwtService;
         this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
            System.out.println("JWT FILTER RUNNING: " + request.getRequestURI());
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            boolean valid = jwtService.isTokenValid(token);

           if (valid) {
    String email = jwtService.extractEmail(token);

    User user = userRepository.findByEmail(email).orElse(null);

    if (user != null) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_" + user.getRole())
                        )
                );

                System.out.println("Authenticated email: " + user.getEmail());
                System.out.println("User email: " + user.getEmail());
System.out.println("User role from database: " + user.getRole());
                System.out.println("Granted authority: " + authentication.getAuthorities());

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }
}


            filterChain.doFilter(request, response);
        }

}