package com.projects.banking.Controllers;

import com.projects.banking.Entities.UserEntity;
import com.projects.banking.Helpers.IbanGenerator;
import com.projects.banking.Repositories.UserRepository;
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
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@Valid @RequestBody UserEntity userEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body((UserEntity) bindingResult.getAllErrors());
        }
        //create IBAN account number
        userEntity.setIBAN(IbanGenerator.generateIban("NL","DASD", "2131321313"));
        userRepository.save(userEntity);
        return ResponseEntity.ok(userEntity);
    }

    @PostMapping("/logon")
    public String logOn() {

        return "";
    }
}
