package com.projects.banking.Entities;

import com.projects.banking.Validators.ValidUserRegister;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ValidUserRegister
@Table(name = "users")
public class UserEntity {
    @Id
    // Primary Key
    @GeneratedValue(strategy = GenerationType.AUTO)
    // AutoIncrement Values
    private Long id;
    /**
     *  IBAN Account Number (Unique)
     * */
    @Column(unique = true, nullable = false)
    private String IBAN;
    @NotBlank(message = "Name Field cannot be empty.")
    private String name;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Name Field cannot be empty.")
    private String username;
    private String address;
    @Column(nullable = false)
    @NotBlank(message = "Password Field cannot be empty.")
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
