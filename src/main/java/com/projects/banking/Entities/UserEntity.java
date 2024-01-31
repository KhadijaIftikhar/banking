package com.projects.banking.Entities;

import jakarta.persistence.*;
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
@Table(name = "users")
public class UserEntity {
    @Id
    // Primary Key
    @GeneratedValue
    // AutoIncrement Values
    private Long id;
    /**
     *  IBAN Account Number (Unique)
     * */
    @Column(unique = true, nullable = false)
    private String IBAN;
    private String name;
    @Column(unique = true, nullable = false)
    private String username;
    private String address;
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
