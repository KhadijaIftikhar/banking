package com.projects.banking.Controllers;

import com.google.gson.Gson;
import com.projects.banking.DTO.OTPRequest;
import com.projects.banking.DTO.UserRequest;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.ExternalAPIServices.DTO.IPInfoResponse;
import com.projects.banking.ExternalAPIServices.IPInfoService;
import com.projects.banking.ExternalAPIServices.SMSGatewayCenterService;
import com.projects.banking.IntergratedServices.OTPService;
import com.projects.banking.Repositories.UserRepository;
import com.projects.banking.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.projects.banking.ExternalAPIServices.TwilioService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@RestController
public class AuthController {



    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        try {
            // Country Validation By IP Address
            String countryCode = IPInfoService.getCountryCode();
            if(!("NL".equals(countryCode) || "BE".equals(countryCode) || "PK".equals(countryCode))){
                return ResponseEntity.ok("Sorry, you're not eligible for this registration.");
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
            return ResponseEntity.badRequest().body("Something went wrong."+ userRequest.getMobileNumber());
        }
        return ResponseEntity.ok("Customer has been Created but need to be Verify by OTP.");
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<?> verifyOTP (@Valid @RequestBody OTPRequest otpRequest) {

        try {
            UserEntity userEntity = userService.findCustomerByUsername(otpRequest.getUsername());
            if(userEntity.getId() != 0) {
                TwilioService.VerifyOTP(userEntity.getMobileNumber(), otpRequest.getOTP());
            } else {
                return ResponseEntity.ok("Oops, Username not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Something went wrong.");
        }
        return ResponseEntity.ok("Congrats! Account has been registered.");
    }

    @PostMapping("/logon")
    public String logOn() {

        return "";
    }
}
