package com.projects.banking.ExternalAPIServices;

import com.google.gson.Gson;
import com.projects.banking.ExternalAPIServices.DTO.IPInfoResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class IPInfoService {


    private static final String ACCESS_TOKEN_KEY = "fe5137c48716cc";
    private static IPInfoResponse ipInfoResponse;
    public static String getCountryCode() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://ipinfo.io/?token="+ ACCESS_TOKEN_KEY))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            ipInfoResponse = gson.fromJson(response.body(), IPInfoResponse.class);
            return ipInfoResponse.getCountry();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Invalid";
    }
}
