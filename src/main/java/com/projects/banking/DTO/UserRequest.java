package com.projects.banking.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String name;
    @NotEmpty(message = "Username Field cannot be empty.")
    private String username;
    private String address;
    // Optional
    private String idDocument;
    @NotEmpty(message="Mobile Number cannot be empty.")
    private String mobileNumber = "+923018675410";
    @NotNull
    private LocalDate dateOfBirth;
}
