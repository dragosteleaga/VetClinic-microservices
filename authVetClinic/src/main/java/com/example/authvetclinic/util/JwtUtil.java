package com.example.authvetclinic.util;

import com.example.authvetclinic.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    private final long expirationMs = 3600000;
    private static final String SECRET = "pisicaFollyFacemiaudeslAgeamcandvedepasarIsa";

    private Key getSigningKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email, Role role, Long id) {
        Key key = getSigningKey();
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
        String token=Jwts.builder()
                .setSubject(email)
                .claim("id", id)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
        System.out.println("Generated Token: [" + token + "]");
        System.out.println("Token Length: " + token.length());
        System.out.println("Base64 Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Key key = new SecretKeySpec("pisicaFollyFacemiaudeslAgeamcandvedepasarIsa".getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // if parsing succeeds, token is valid
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Invalid JWT Token: " + e.getMessage());
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }

}
