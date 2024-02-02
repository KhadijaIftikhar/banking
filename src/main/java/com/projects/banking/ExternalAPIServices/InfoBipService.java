package com.projects.banking.ExternalAPIServices;


import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Send an SMS message directly by calling HTTP endpoint.
 *
 * THIS CODE EXAMPLE IS READY BY DEFAULT. HIT RUN TO SEND THE MESSAGE!
 *
 * Send SMS API reference: https://www.infobip.com/docs/api#channels/sms/send-sms-message
 * See Readme file for details.
 */
public class InfoBipService {

    private static final String BASE_URL = "https://e121rn.api.infobip.com";
    private static final String API_KEY = "App 3e3568bbaabd1d4a84bd78f82cbc06d7-9d502798-cc8d-43f6-8c91-d31fba1f40a3";

    protected static final String MEDIA_TYPE = "application/json";
    protected static Request prepareHttpRequest(RequestBody body) {
        return new Request.Builder()
                .url(String.format("%s/sms/2/text/advanced", BASE_URL))
                .method("POST", body)
                .addHeader("Authorization", API_KEY)
                .addHeader("Content-Type", MEDIA_TYPE)
                .addHeader("Accept", MEDIA_TYPE)
                .build();
    }
}