package com.nts.users.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;

import java.security.SignatureException;
import java.util.Date;



@Component
public final class JwtUtils {


    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private Key key;

    @PostConstruct
    private void init() {
        // initialize key after @Value injection
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public    String generateToken(String userId, String role, String email, String username){
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .claim("email", email)
                .claim("username", username)
                .setIssuer("Auth")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1000L* 60 * 60 * 24 * 7)))
                .signWith(key)
                .compact();

    }

    public  Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public  boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // invalid, expired, or tampered
        }
    }
    public  Claims extractUserId(String token) throws SignatureException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}

