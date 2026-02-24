package com.cusco.limpio.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
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

import com.cusco.limpio.security.JwtAuthFilter;
import com.cusco.limpio.security.JwtTokenProvider;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider tokenProvider,
            UserDetailsService userDetailsService) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(tokenProvider, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {

                    // Rutas completamente públicas
                    auth.requestMatchers("/api/health/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();

                    // Registro: solo POST /api/users es público. GET /api/users requiere
                    // autenticación.
                    auth.requestMatchers(HttpMethod.POST, "/api/users").permitAll();

                    // Login: POST /api/users/login es público
                    auth.requestMatchers(HttpMethod.POST, "/api/users/login").permitAll();

                    // Consola H2 solo en perfil de desarrollo
                    if (isDevelopmentProfile()) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    // Cualquier otra petición requiere autenticación
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        if (isDevelopmentProfile()) {
            http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        }

        return http.build();
    }

    private boolean isDevelopmentProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }
}
