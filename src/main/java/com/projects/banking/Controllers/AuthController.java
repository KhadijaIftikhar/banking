package com.projects.banking.Controllers;

import com.projects.banking.DTO.CustomerOverviewRequest;
import com.projects.banking.DTO.CustomerOverviewResponse;
import com.projects.banking.DTO.OTPRequest;
import com.projects.banking.DTO.UserRequest;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.ExternalAPIServices.IPInfoService;
import com.projects.banking.Helpers.AgeCalculator;
import com.projects.banking.Services.UserService;
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



    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        try {
            // Country Validation By IP Address
            String countryCode = IPInfoService.getCountryCode();
            if(!("NL".equals(countryCode) || "BE".equals(countryCode) || "PK".equals(countryCode))){
                return ResponseEntity.ok("Sorry, your country not eligible for this registration.");
            }

            LocalDate currentDate = LocalDate.now();
            if(AgeCalculator.calculateAge(userRequest.getDateOfBirth(), currentDate) < 18) {
                return ResponseEntity.ok("Sorry, your age not eligible for this registration.");
            }

            UserEntity existingUserCheck = userService.findCustomerByUsername(userRequest.getUsername());
            if(existingUserCheck != null) {
                return ResponseEntity.ok("Oops, username already exists.");
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

    @PostMapping("/logIn")
    public String logIn() {

        return "";
    }

    @GetMapping("/overview")
    public ResponseEntity<?> overview(CustomerOverviewRequest customerOverviewRequest, BindingResult bindingResult) {
        try{
            // getting any validation errors while creating customer registration
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }


            UserEntity userEntity = userService.findCustomerByUsername(customerOverviewRequest.getUsername());
            if(userEntity != null) {
                return ResponseEntity.ok(userEntity);
            } else {
                return ResponseEntity.ok("Oops, Username not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong."+ e.getMessage());
        }
    }
}
