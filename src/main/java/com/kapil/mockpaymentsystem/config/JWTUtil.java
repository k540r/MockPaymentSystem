package com.kapil.mockpaymentsystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    private final String SECRET = "kapilSecretKeykapilSecretKey123456";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    //Generate Token (username + role)
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

   
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Common method
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String generateRefreshToken(String username) {
    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new java.util.Date())
            .setExpiration(new java.util.Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 days
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
}