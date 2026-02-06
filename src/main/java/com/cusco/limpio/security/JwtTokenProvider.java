package com.cusco.limpio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Proveedor de tokens JWT usando la biblioteca jjwt 0.12.x
 * Implementa generación, validación y extracción de información de tokens JWT
 */
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:changeit}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long jwtExpirationMs;

    /**
     * Genera una SecretKey compatible con HMAC-SHA256 a partir del secreto configurado
     */
    private SecretKey getSigningKey() {
        // Aseguramos que la clave tenga al menos 256 bits para HS256
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT para el usuario dado
     * @param subject El identificador del usuario (generalmente email)
     * @return Token JWT firmado
     */
    public String generateToken(String subject) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(exp)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Valida un token JWT
     * @param token Token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // Token inválido, expirado o malformado
            return false;
        }
    }

    /**
     * Extrae el subject (usuario) del token JWT
     * @param token Token JWT
     * @return Subject del token (email del usuario)
     */
    public String getSubject(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
