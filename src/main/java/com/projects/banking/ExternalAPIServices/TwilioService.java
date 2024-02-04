package com.projects.banking.ExternalAPIServices;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.Service;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.core.env.Environment;

public class TwilioService {

    public static String TWILIO_ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static String TWILIO_AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static String verifySid = "VA4f06eaa341c2aa5c5734adedd86f696a";

    private static Environment env;

    private static Service service;
    public TwilioService() {
        Twilio.init("AC8401d77a0137656a1dd504302fc42e22", "ae71b5f30129181fd6c12f5522669e45");
        service = Service.creator("Bank OTP Service").create();
    }
    public static String initiateTwilio(String mobileNumber) {

        Verification verification = Verification.creator(
                        service.getSid(), // this is your verification sid
                        mobileNumber, //this is your Twilio verified recipient phone number
                        "sms") // this is your channel type
                .create();
        return verification.getStatus();
    }

    public static String VerifyOTP (String mobileNumber,String OTP){

        VerificationCheck verificationCheck = VerificationCheck.creator(
                        service.getSid())
                .setTo(mobileNumber)
                .setCode(OTP)
                .create();

        return verificationCheck.getStatus();
    }
}
