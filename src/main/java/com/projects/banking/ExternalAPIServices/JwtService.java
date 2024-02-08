package com.projects.banking.ExternalAPIServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String createJwt(String subject) {
        long expirationTimeMillis = 3600000; // 1 hour
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMillis))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseJwt(String jwt) throws SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean isTokenExpired(String jwt) {
        Claims claims = parseJwt(jwt);
        Date expirationDate = claims.getExpiration();
        return expirationDate != null && expirationDate.before(new Date());
    }

    public Claims validateToken(String token) throws SignatureException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (SignatureException e) {
            // The token signature is not valid
            throw new SignatureException("Invalid token signature");
        } catch (Exception e) {
            // Other exceptions, e.g., token expired
            throw new RuntimeException("Token validation failed", e);
        }
    }
}
