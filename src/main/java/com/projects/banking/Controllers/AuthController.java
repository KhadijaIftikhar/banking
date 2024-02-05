package com.projects.banking.Controllers;

import com.projects.banking.DTO.*;
import com.projects.banking.Entities.AuthAccessTokenEntity;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.ExternalAPIServices.IPInfoService;
import com.projects.banking.ExternalAPIServices.JwtService;
import com.projects.banking.Helpers.AgeCalculator;
import com.projects.banking.Services.AuthAccessTokenService;
import com.projects.banking.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.projects.banking.ExternalAPIServices.TwilioService;

import java.time.LocalDate;

@RestController
@Tag(name = "Authentication APIs")
public class AuthController {



    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthAccessTokenService authAccessTokenService;

    @Operation(summary = "Register API", description = "Register API with OTP verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer has been Created but need to be Verify by OTP."),
            @ApiResponse(responseCode = "400", description = "Sorry, you're not eligible for this registration.")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        try {
            // Country Validation By IP Address
            String countryCode = IPInfoService.getCountryCode();
            if(!("NL".equals(countryCode) || "BE".equals(countryCode))){
                return ResponseEntity.badRequest().body("Sorry, your country not eligible for this registration.");
            }

            LocalDate currentDate = LocalDate.now();
            if(AgeCalculator.calculateAge(userRequest.getDateOfBirth(), currentDate) < 18) {
                return ResponseEntity.badRequest().body("Sorry, your age not eligible for this registration.");
            }

            UserEntity existingUserCheck = userService.findCustomerByUsername(userRequest.getUsername());
            if(existingUserCheck != null) {
                return ResponseEntity.badRequest().body("Oops, username already exists.");
            }

            // getting any validation errors while creating customer registration
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }

            // getting any errors while creating customer registration
            UserEntity userEntity = userService.saveCustomer(userRequest);

            if(userEntity.getId() != 0) {
                TwilioService.initiateTwilio(userRequest.getMobileNumber());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong.");
        }
        return ResponseEntity.ok("Customer has been Created but need to be Verify by OTP.");
    }

    @Operation(summary = "OTP API", description = "OTP verification By Phone.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Congrats! Account has been registered."),
            @ApiResponse(responseCode = "400", description = "Something went wrong.")
    })
    @PostMapping("/verifyOTP")
    public ResponseEntity<?> verifyOTP (@RequestBody @Valid OTPRequest otpRequest, BindingResult bindingResult) {

        try {
            // getting any validation errors while creating customer registration
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }

            UserEntity userEntity = userService.findCustomerByUsername(otpRequest.getUsername());
            if(userEntity != null) {
                TwilioService.VerifyOTP(userEntity.getMobileNumber(), otpRequest.getOTP());
                if(userService.updateCustomerVerification(userEntity,1) == null) {
                    return ResponseEntity.ok("Oops! Not Updated User.");
                }
                return ResponseEntity.ok("Congrats! Account has been registered.");
            } else {
                return ResponseEntity.ok("Oops, Username not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong."+ e.getMessage());
        }
    }

    @Operation(summary = "LogIn API", description = "Login API with Using Default password - 123456")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return User Object with Token Information."),
            @ApiResponse(responseCode = "400", description = "Oops, Invalid Credentials.")
    })
    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        try{
            // getting any validation errors while creating customer registration
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }
            // BYPASS Password for Now
            UserEntity userEntity = userService.findByCustomerCredentials(loginRequest.getUsername(), loginRequest.getPassword());
            if(userEntity != null) {
                AuthAccessTokenEntity token = authAccessTokenService.saveToken(loginRequest,userEntity);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setUserEntity(userEntity);
                loginResponse.setToken(token.getToken());
                loginResponse.setExpiryAt(token.getExpiryAt());
                return ResponseEntity.ok(loginResponse);
            } else {
                return ResponseEntity.badRequest().body("Oops, Invalid Credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong."+ e.getMessage());
        }
    }

    @Operation(summary = "OverView API", description = "Customer detailed overview.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Customer Object"),
            @ApiResponse(responseCode = "400", description = "Oops, Invalid User!.")
    })
    @GetMapping("/overview")
    public ResponseEntity<?> overview(CustomerOverviewRequest customerOverviewRequest, BindingResult bindingResult) {
        try{
            // getting any validation errors while creating customer registration
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }

            if(jwtService.isTokenExpired(customerOverviewRequest.getToken())) {
                return ResponseEntity.badRequest().body("Oops, time-out. token has been expired.");
            }

            UserEntity userEntity = userService.findCustomerByUsername(customerOverviewRequest.getUsername());

            String token = authAccessTokenService.ValidatedTokenFormDatabase(userEntity.getId()).getToken();
            if(jwtService.validateToken(customerOverviewRequest.getToken()).getId().isEmpty() ||  jwtService.validateToken(token).getId().isEmpty()) {
                return ResponseEntity.badRequest().body("Oops, Invalid User!.");
            }

            if(userEntity != null) {
                return ResponseEntity.ok(userEntity);
            } else {
                return ResponseEntity.badRequest().body("Oops, Username not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong."+ e.getMessage());
        }
    }
}
