package com.projects.banking.Configurations;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
@Configuration
public class JwtConfig {
    @Bean
    public SecretKey secretKey() {
        // Generate a secret key. You can load it from a secure location in production.
        return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }
}
