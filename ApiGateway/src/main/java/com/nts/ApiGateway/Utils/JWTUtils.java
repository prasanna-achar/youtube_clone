package com.nts.ApiGateway.Utils;

import com.nts.ApiGateway.Utils.Interface.AuthUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;


@Component
public class JWTUtils {


    @Value("${jwt.secret}")
    private  String SECRET;

    private Key key;

    @PostConstruct
    private void init() {
        // initialize key after @Value injection
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }


    public Map<String, Object> validateAndExtract(String token) {
        return new AuthUtil() {
            @Override
            public Map<String, Object> validateAndExtract(String token) {
                Map<String, Object> details = new HashMap<>();
                try {
                    Claims claims = Jwts.parser()
                            .setSigningKey(key)
                            .parseClaimsJws(token)
                            .getBody();
                    details.put("id", claims.getSubject());
                    details.put("email", claims.get("email"));
                    details.put("role", claims.get("role"));
                    details.put("username", claims.get("username"));
                } catch (Exception e) {
                    System.out.println("JWT validation failed: " + e.getMessage());
                }
                return details;
            }
        }.validateAndExtract(token);
    }
}
