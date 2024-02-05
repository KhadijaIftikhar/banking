package com.projects.banking.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    private String name;
    @Column(unique = true, nullable = false)
    private String username;
    private String address;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    // idDocument Path
    private String idDocument;
    // Optional
    private LocalDate dateOfBirth;
    //
    private String mobileNumber;
    //
    private double balance;
    //
    private String accountType;
    @Column(nullable = false)
    private Integer isVerified;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
