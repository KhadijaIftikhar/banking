package com.projects.banking.Services;

import com.projects.banking.DTO.LoginRequest;
import com.projects.banking.Entities.AuthAccessTokenEntity;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.ExternalAPIServices.JwtService;
import com.projects.banking.Repositories.AuthAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthAccessTokenService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthAccessTokenRepository authAccessTokenRepository;
    public AuthAccessTokenEntity saveToken(LoginRequest loginRequest, UserEntity userEntity) {
        AuthAccessTokenEntity token = new AuthAccessTokenEntity();
        token.setUserId(userEntity.getId());
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dateTimeAfterOneHour = currentDateTime.plusHours(1);
        token.setExpiryAt(dateTimeAfterOneHour);
        token.setToken(jwtService.createJwt(loginRequest.getUsername()));
        return authAccessTokenRepository.save(token);
    }

    public AuthAccessTokenEntity ValidatedTokenFormDatabase(Long id) {
        return authAccessTokenRepository.findByUserId(id);
    }
}
