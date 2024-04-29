package com.cthulhu.services;

import com.cthulhu.models.LoginData;
import com.cthulhu.models.LoginResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpService {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static ResponseEntity<LoginResponse> loginRequest(String name, String password) throws NoSuchAlgorithmException {
        return makeRequest(name, password, "login", LoginResponse.class);
    }

    public static HttpStatusCode registerRequest(String name, String password) throws NoSuchAlgorithmException {
        var response = makeRequest(name, password, "register", String.class);
        return response.getStatusCode();
    }

    private static <T> ResponseEntity<T> makeRequest(String name, String password, String endpoint, Class<T> responseType) throws NoSuchAlgorithmException {
        String hash = getHash(password);
        LoginData loginData = new LoginData(name, hash);
        String url = "http://127.0.0.1:8080/" + endpoint;
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
