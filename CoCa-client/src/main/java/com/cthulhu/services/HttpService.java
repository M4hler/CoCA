package com.cthulhu.services;

import com.cthulhu.models.LoginData;
import com.cthulhu.models.LoginResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpService {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static HttpStatusCode healthCheckRequest(String address) {
        var response = makeHealthCheckRequest(address);
        return response.getStatusCode();
    }

    public static ResponseEntity<LoginResponse> loginRequest(String name, String password, String address) throws NoSuchAlgorithmException {
        return makeRequest(name, password, "login", LoginResponse.class, address);
    }

    public static HttpStatusCode registerRequest(String name, String password, String address) throws NoSuchAlgorithmException {
        var response = makeRequest(name, password, "register", String.class, address);
        return response.getStatusCode();
    }

    private static ResponseEntity<String> makeHealthCheckRequest(String address) {
        try {
            String url = address + "health";
            return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), String.class);
        }
        catch(Exception e) {
            System.out.println("Error: " + e);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private static <T> ResponseEntity<T> makeRequest(String name, String password, String endpoint, Class<T> responseType,
                                                     String address) throws NoSuchAlgorithmException {
        String hash = getHash(password);
        LoginData loginData = new LoginData(name, hash);
        String url = address + endpoint;
        HttpEntity<LoginData> request = new HttpEntity<>(loginData);

        try {
            return restTemplate.exchange(url, HttpMethod.POST, request, responseType);
        }
        catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAs(responseType));
        }
    }

    private static String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(hashBytes, StandardCharsets.UTF_8);
    }
}
