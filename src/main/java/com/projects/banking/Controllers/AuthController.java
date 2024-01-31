package com.projects.banking.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {


    @PostMapping("/register")
    public String register() {

        //create IBAN account number
        return "";
    }

    @PostMapping("/logon")
    public String logOn() {

        return "";
    }
}
