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

    public static String initiateTwilio(String mobileNumber) {
        Twilio.init("AC8401d77a0137656a1dd504302fc42e22", "28b81f594efabe6aa1b68f71edc50a68");
       // Service service = Service.creator("Bank OTP Service").create();
        Verification verification = Verification.creator(
                        verifySid, // this is your verification sid
                        mobileNumber, //this is your Twilio verified recipient phone number
                        "sms") // this is your channel type
                .create();
        return verification.getStatus();
    }

    public static String VerifyOTP (String mobileNumber,String OTP){
        Twilio.init("AC8401d77a0137656a1dd504302fc42e22", "28b81f594efabe6aa1b68f71edc50a68");
        VerificationCheck verificationCheck = VerificationCheck.creator(
                        verifySid)
                .setTo(mobileNumber)
                .setCode(OTP)
                .create();
        System.out.println("Status:: "+ verificationCheck.getStatus());
        return verificationCheck.getStatus();
    }
}
