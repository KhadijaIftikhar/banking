package com.projects.banking.Services;

import com.projects.banking.DTO.TokenRequest;
import com.projects.banking.Entities.AuthAccessTokenEntity;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.ExternalAPIServices.JwtService;
import com.projects.banking.Repositories.AuthAccessTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Transactional
public class AuthAccessTokenService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthAccessTokenRepository authAccessTokenRepository;
    public AuthAccessTokenEntity saveToken(TokenRequest tokenRequest, UserEntity userEntity) {
        authAccessTokenRepository.deleteAllByUserId(userEntity.getId());
        AuthAccessTokenEntity token = new AuthAccessTokenEntity();
        token.setUserId(userEntity.getId());
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dateTimeAfterOneHour = currentDateTime.plusMinutes(1);
        token.setExpiryAt(dateTimeAfterOneHour);
        token.setToken(jwtService.createJwt(tokenRequest.getUsername(), 60000L));
        return authAccessTokenRepository.save(token);
    }

    public AuthAccessTokenEntity findTokenByUserId(Long id) {
        return authAccessTokenRepository.findByUserId(id);
    }

    public boolean isTokenExpired (String token) {
        Claims claims = jwtService.parseJwt(token);
        Instant instant = claims.getExpiration().toInstant();
        LocalDateTime expirationDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return expirationDate.isBefore(now);
    }
}
