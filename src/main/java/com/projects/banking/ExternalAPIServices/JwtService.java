package com.projects.banking.ExternalAPIServices;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.Services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
@Service
public class JwtService {

    private final SecretKey secretKey;

    private Long expirationTimeMillis = 3600000L;

    public JwtService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String createJwt(String subject, Long expirationTimeMillis) {
        this.expirationTimeMillis = expirationTimeMillis;
        System.out.println("secretKey:: "+secretKey);
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + this.expirationTimeMillis))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseJwt(String jwt) throws SignatureException {
        System.out.println("secretKey:: "+secretKey);
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean isTokenExpired(String jwt) {
        System.out.println(jwt);
        Claims claims = parseJwt(jwt);
//        String expireDateString = "2024-02-12T21:38:00";
//        LocalDateTime expireDateTime = LocalDateTime.parse(expireDateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Instant instant = claims.getExpiration().toInstant();
//        ZoneId zone = ZoneId.of("America/Los_Angeles");
        LocalDateTime expirationDate = LocalDateTime.ofInstant(instant,ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        System.out.println("expirationDate:: "+ expirationDate);
        System.out.println("now:: "+ now);
        System.out.println("isBefore:: "+ expirationDate.isBefore(now));
        return expirationDate != null && expirationDate.isBefore(now);
    }

    public Claims validateToken(String token, UserService userService) throws SignatureException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            UserEntity userEntity = userService.findCustomerByUsername(claims.getSubject());
            if(userEntity == null) {
                throw new SignatureException("Invalid token signature");
            }
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
