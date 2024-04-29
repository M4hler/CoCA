package com.cthulhu.services;

import com.cthulhu.models.LoginData;
import com.cthulhu.models.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpService {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static ResponseEntity<LoginResponse> loginRequest(String name, String password) throws NoSuchAlgorithmException, InterruptedException, IOException {
        return makeRequest(name, password, "login");
    }

    public static HttpStatus registerRequest(String name, String password) throws NoSuchAlgorithmException, InterruptedException, IOException {
        HttpResponse<String> response = makeRegisterRequest(name, password, "register");
        String status = response.body().replaceAll("\"", "");

        if (status.equals(HttpStatus.CONFLICT.name())) {
            return HttpStatus.CONFLICT;
        }

        if (!status.equals(HttpStatus.OK.name())) {
            return HttpStatus.valueOf(response.statusCode());
        }

        return HttpStatus.OK;
    }

    private static ResponseEntity<LoginResponse> makeRequest(String name, String password, String endpoint) throws NoSuchAlgorithmException, IOException, InterruptedException {
        String hash = getHash(password);
        LoginData loginData = new LoginData(name, hash);
        String url = "http://127.0.0.1:8080/" + endpoint;
        HttpEntity<LoginData> request = new HttpEntity<>(loginData);
        return restTemplate.exchange(url, HttpMethod.POST, request, LoginResponse.class);
    }

    private static HttpResponse<String> makeRegisterRequest(String name, String password, String endpoint) throws NoSuchAlgorithmException, IOException, InterruptedException {
        String hash = getHash(password);
        LoginData loginData = new LoginData(name, hash);
        URI uri = URI.create("http://127.0.0.1:8080/" + endpoint);
        HttpClient client = HttpClient.newHttpClient();
        String payload = new ObjectMapper().writeValueAsString(loginData);
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(hashBytes, StandardCharsets.UTF_8);
    }
}
