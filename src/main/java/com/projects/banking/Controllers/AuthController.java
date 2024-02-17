package com.projects.banking.Controllers;

import com.projects.banking.DTO.*;
import com.projects.banking.Entities.AuthAccessTokenEntity;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.ExternalAPIServices.IPInfoService;
import com.projects.banking.ExternalAPIServices.JwtService;
import com.projects.banking.Helpers.AgeCalculator;
import com.projects.banking.Services.AuthAccessTokenService;
import com.projects.banking.Services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
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
            UserResponse userResponse = new UserResponse();
            userResponse.setUsername(userEntity.getUsername());
            userResponse.setPassword(userEntity.getPassword());
            userResponse.setMessage("Customer has been Created but need to be Verify by OTP.");
            if(userEntity.getId() != 0) {
                TwilioService.initiateTwilio(userRequest.getMobileNumber());
            }
            return ResponseEntity.ok(userResponse);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong.");
        }
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

    @Operation(summary = "TOKEN API", description = "TOKEN API with using Default password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Token."),
            @ApiResponse(responseCode = "400", description = "Oops! Invalid Credentials.")
    })
    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody TokenRequest tokenRequest, BindingResult bindingResult) {
        try{
            // getting any validation errors while creating customer registration
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }

            UserEntity userEntity = userService.findByCustomerCredentials(tokenRequest.getUsername(), tokenRequest.getPassword());
            if(userEntity != null) {
                AuthAccessTokenEntity token = authAccessTokenService.saveToken(tokenRequest,userEntity);
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setToken(token.getToken());
                return ResponseEntity.ok(tokenResponse);
            } else {
                return ResponseEntity.badRequest().body("Oops! Invalid Credentials.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Oops! Something went wrong.");
        }
    }

    @Operation(summary = "Login API", description = "Login API with using token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return Success Response."),
            @ApiResponse(responseCode = "400", description = "Oops! Invalid Token.")
    })
    @PostMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        try{
            // getting any validation errors while creating customer registration
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }

            if(jwtService.isTokenExpired(loginRequest.getToken())) {
                return ResponseEntity.badRequest().body("Oop! Time-out. Token has been expired.");
            }

            UserEntity userEntity = userService.findCustomerByUsername(loginRequest.getUsername());
            if(userEntity == null) {
                return ResponseEntity.badRequest().body("Oops! User not found.");
            }

            return ResponseEntity.ok("Success! Welcome, "+userEntity.getUsername());
        }  catch (ExpiredJwtException e) {
            return ResponseEntity.badRequest().body("Oop! Time-out. Token has been expired.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error! Invalid token.");
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
            if(userEntity == null) {
                return ResponseEntity.badRequest().body("Oops! Username not found.");
            }

            CustomerOverviewResponse customerOverviewResponse = new CustomerOverviewResponse();
            customerOverviewResponse.setAccountNumber(userEntity.getIBAN());
            customerOverviewResponse.setAccountType(userEntity.getAccountType());
            customerOverviewResponse.setBalance(userEntity.getBalance());
            customerOverviewResponse.setCurrency("Euro");
            return ResponseEntity.ok(customerOverviewResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Oops! Something went wrong.");
        }
    }
}
