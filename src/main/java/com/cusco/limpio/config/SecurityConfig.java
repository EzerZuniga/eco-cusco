package com.cusco.limpio.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cusco.limpio.security.JwtAuthFilter;
import com.cusco.limpio.security.JwtTokenProvider;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private static final List<String> PUBLIC_DOCUMENTATION_ENDPOINTS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html");

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
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> {
                    headers.contentTypeOptions(cto -> {
                    });
                    headers.xssProtection(xss -> {
                    });
                    if (isLocalProfile()) {
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                    } else {
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny);
                    }
                })
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> {
                    // Permite preflight CORS sin autenticación
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    // Rutas completamente públicas
                    auth.requestMatchers("/api/health/**").permitAll();
                    auth.requestMatchers(PUBLIC_DOCUMENTATION_ENDPOINTS.toArray(String[]::new)).permitAll();

                    // Registro: solo POST /api/users es público
                    auth.requestMatchers(HttpMethod.POST, "/api/users").permitAll();

                    // Login: POST /api/users/login es público
                    auth.requestMatchers(HttpMethod.POST, "/api/users/login").permitAll();

                    // Consola H2 solo en entornos locales.
                    if (isLocalProfile()) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }

                    // Cualquier otra petición requiere autenticación
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private boolean isLocalProfile() {
        return environment.acceptsProfiles(Profiles.of("dev", "test"));
    }
}
