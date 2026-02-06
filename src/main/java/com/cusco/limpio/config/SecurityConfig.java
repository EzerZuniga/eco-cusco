package com.cusco.limpio.config;

import com.cusco.limpio.security.JwtAuthFilter;
import com.cusco.limpio.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * Configuración de seguridad de Spring Security
 * Implementa autenticación JWT stateless y autorización basada en roles
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final Environment environment;

    public SecurityConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(tokenProvider, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    // Endpoints públicos
                    auth.requestMatchers(
                            "/api/health/**",
                            "/api/users",
                            "/api/users/login",
                            "/swagger-ui/**",
                            "/v3/api-docs/**",
                            "/swagger-ui.html"
                    ).permitAll();

                    // Permitir H2 console solo en perfil dev
                    if (isDevelopmentProfile()) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    // Todos los demás endpoints requieren autenticación
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Desabilitar frame options solo en desarrollo (necesario para H2 console)
        if (isDevelopmentProfile()) {
            http.headers(headers -> 
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            );
        }

        return http.build();
    }

    /**
     * Verifica si el perfil activo es desarrollo
     */
    private boolean isDevelopmentProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        return Arrays.asList(activeProfiles).contains("dev");
    }
}
