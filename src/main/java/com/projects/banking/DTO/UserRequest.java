package com.projects.banking.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String name;
    @NotBlank(message = "Username Field cannot be empty.")
    private String username;
    private String address;
    @NotBlank(message = "CountryName Field cannot be empty.")
    private String countryName;
    // Optional
    private String idDocument;

    @NotBlank(message="Mobile Number cannot be empty.")
    private String mobileNumber;

    private LocalDate dateOfBirth;

}
