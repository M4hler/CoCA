package com.cthulhu.services;

import com.cthulhu.models.LoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpService {
    public static HttpStatus loginRequest(String name, String password) throws NoSuchAlgorithmException, InterruptedException,IOException {
        HttpResponse<String> response = makeRequest(name, password, "login");
        String status = response.body().replaceAll("\"", "");

        if(status.equals(HttpStatus.NOT_FOUND.name())) {
            return HttpStatus.NOT_FOUND;
        }

        if(status.equals(HttpStatus.FORBIDDEN.name())) {
            return HttpStatus.FORBIDDEN;
        }

        if(!status.equals(HttpStatus.OK.name())) {
            return HttpStatus.valueOf(response.statusCode());
        }

        return HttpStatus.OK;
    }

    public static HttpStatus registerRequest(String name, String password) throws NoSuchAlgorithmException, InterruptedException, IOException {
        HttpResponse<String> response = makeRequest(name, password, "register");
        String status = response.body().replaceAll("\"", "");

        if (status.equals(HttpStatus.CONFLICT.name())) {
            return HttpStatus.CONFLICT;
        }

        if (!status.equals(HttpStatus.OK.name())) {
            return HttpStatus.valueOf(response.statusCode());
        }

        return HttpStatus.OK;
    }

    private static HttpResponse<String> makeRequest(String name, String password, String endpoint) throws NoSuchAlgorithmException, IOException, InterruptedException {
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
