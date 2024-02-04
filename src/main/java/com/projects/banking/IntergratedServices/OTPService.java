package com.projects.banking.IntergratedServices;

import com.projects.banking.ExternalAPIServices.InfoBipService;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OTPService extends InfoBipService{


    private static final String SENDER = "Bank - OTP";
//    private static final String RECIPIENT = "923018675410";
    private static final String MESSAGE_TEXT = "Thanks For choosing us. your OTP is :";

    public static String sendOTP(String phoneNumber, String OTP) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        String bodyJson = String.format("{\"messages\":[{\"from\":\"%s\",\"destinations\":[{\"to\":\"%s\"}],\"text\":\"%s\"}]}",
                SENDER,
                phoneNumber,
                MESSAGE_TEXT+OTP
        );

        MediaType mediaType = MediaType.parse(MEDIA_TYPE);
        RequestBody body = RequestBody.create(bodyJson, mediaType);

        Request request = prepareHttpRequest(body);
        Response response = client.newCall(request).execute();

        System.out.println("HTTP status code: " + response.code());
        System.out.println("Response body: " + response.body().string());
        return bodyJson;
    }
}