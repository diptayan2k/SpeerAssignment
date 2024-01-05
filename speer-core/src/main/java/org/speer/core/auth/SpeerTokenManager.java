package org.speer.core.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.speer.core.entities.User;

import java.util.Date;

public class SpeerTokenManager {

    private final String secretKey;

    @Inject
    public SpeerTokenManager(@Named("secretKey") String secretKey) {
        this.secretKey = secretKey;

    }

    public String generateToken(User user) {
        // Build the JWT token
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000)) // Token expires in 10 days
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String validateAndParseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            // Handle token validation errors
            throw new RuntimeException("Error while parsing token!!");
        }
    }
}
