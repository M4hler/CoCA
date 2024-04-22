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
    public static HttpStatus loginRequest(String name, String password) throws NoSuchAlgorithmException, InterruptedException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String hash = new String(hashBytes, StandardCharsets.UTF_8);

        LoginData loginData = new LoginData(name, hash);
        URI uri = URI.create("http://127.0.0.1:8080/register");
        HttpClient client = HttpClient.newHttpClient();
        String payload = new ObjectMapper().writeValueAsString(loginData);
        HttpRequest request = HttpRequest.newBuilder().uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String status = response.body().replaceAll("\"", "");

        if (status.equals(HttpStatus.CONFLICT.name())) {
            return HttpStatus.CONFLICT;
        }

        if (!status.equals(HttpStatus.OK.name())) {
            return HttpStatus.valueOf(response.statusCode());
        }

        return HttpStatus.OK;
    }
}
