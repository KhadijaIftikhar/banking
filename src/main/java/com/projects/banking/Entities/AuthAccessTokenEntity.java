package com.projects.banking.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthAccessTokenEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String token;

    private Long userId;

    private LocalDateTime expiryAt;
}
