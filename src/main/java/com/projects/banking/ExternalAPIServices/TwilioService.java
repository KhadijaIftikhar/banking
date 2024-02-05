package com.projects.banking.ExternalAPIServices;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.core.env.Environment;

public class TwilioService {

    public static String TWILIO_ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static String TWILIO_AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static String verifySid = "VAb9848f4b45ed9c215887140e02bc7c7c";

    private static Environment env;

    public static String initiateTwilio(String mobileNumber) {
        Twilio.init("AC2ffafc9e4be7a1bfde5b0a24f60e3a6c", "8c394b99e7ba287c8955a134a192ecf0");
       // Service service = Service.creator("Bank OTP Service").create();
        Verification verification = Verification.creator(
                        verifySid, // this is your verification sid
                        mobileNumber, //this is your Twilio verified recipient phone number
                        "sms") // this is your channel type
                .create();
        return verification.getStatus();
    }

    public static String VerifyOTP (String mobileNumber,String OTP){
        Twilio.init("AC2ffafc9e4be7a1bfde5b0a24f60e3a6c", "8c394b99e7ba287c8955a134a192ecf0");
        VerificationCheck verificationCheck = VerificationCheck.creator(
                        verifySid)
                .setTo(mobileNumber)
                .setCode(OTP)
                .create();
        System.out.println("Status:: "+ verificationCheck.getStatus());
        return verificationCheck.getStatus();
    }
}
