package com.cthulhu.services;

import com.cthulhu.models.LoginData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpService {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static HttpResponse<String> healthCheckRequest(String address) {
        return makeHealthCheckRequest(address);
    }

    public static HttpResponse<String> loginRequest(String name, String password, String address) throws NoSuchAlgorithmException {
        return makeLoginDataRequest(name, password, address, "/login");
    }

    public static HttpResponse<String> registerRequest(String name, String password, String address) throws NoSuchAlgorithmException {
        return makeLoginDataRequest(name, password, address, "/register");
    }

    private static HttpResponse<String> makeLoginDataRequest(String name, String password, String address, String endpoint) {
        try {
            var hash = getHash(password);
            var loginData = new LoginData(name, hash);
            var requestBody = mapper.writeValueAsString(loginData);
            var uri = new URI(address + endpoint);
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
            return null;
        }
    }

    private static HttpResponse<String> makeHealthCheckRequest(String address) {
        try {
            var uri = new URI(address + "/health");
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception e) {
            System.out.println("Exception: " + e);
            return null;
        }
    }

    private static String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(hashBytes, StandardCharsets.UTF_8);
    }
}
