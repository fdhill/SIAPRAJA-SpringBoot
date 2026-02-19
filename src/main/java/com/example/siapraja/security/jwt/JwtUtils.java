package com.example.siapraja.security.jwt;

import com.example.siapraja.security.MyUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Gunakan secret yang kuat (min 32 karakter)
    private String jwtSecret = "SiaprajaSangatRahasiaSekali12345678901234567890";
    private int jwtExpirationMs = 86400000; // 24 jam

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateJwtToken(Authentication authentication) {
        MyUserDetails userPrincipal = (MyUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parse(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log error jika perlu: "Invalid JWT: " + e.getMessage()
        }
        return false;
    }
}