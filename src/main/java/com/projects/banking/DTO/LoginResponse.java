package com.projects.banking.DTO;

import com.projects.banking.Entities.UserEntity;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private UserEntity userEntity;
    private String token;
    private LocalDateTime expiryAt;
}
