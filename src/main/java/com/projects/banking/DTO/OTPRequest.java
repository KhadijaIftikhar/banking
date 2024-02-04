package com.projects.banking.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPRequest {
    @NotBlank(message = "OTP Field must be not empty.")
    private String OTP;

    @NotBlank(message = "Username Field must be not empty.")
    private String username;
}
