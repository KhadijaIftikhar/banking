package com.projects.banking.Controllers;

import com.projects.banking.DTO.UserRequest;
import com.projects.banking.Entities.UserEntity;
import com.projects.banking.IntergratedServices.OTPService;
import com.projects.banking.Repositories.UserRepository;
import com.projects.banking.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {



    @Autowired
    private UserService userService;

    @PostMapping("/register")

    public ResponseEntity<?> register(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
            }
            UserEntity userEntity = userService.saveCustomer(userRequest);
//            if(userEntity != null) {
//                OTPService.sendOTP(userEntity.getMobileNumber(), userEntity.getOTP());
//            }
            return ResponseEntity.ok("Customer has been Successfully Register.");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/logon")
    public String logOn() {

        return "";
    }
}
