package com.projects.banking.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import static org.yaml.snakeyaml.tokens.Token.ID.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class OTPRequest {

    @NotEmpty(message = "OTP Field must be not empty.")
    private String OTP;

    @NotEmpty(message = "Username Field must be not empty.")
    private String username;
}
