package com.cthulhu.controllers;

import com.cthulhu.models.LoginData;
import com.cthulhu.views.LoginView;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
public class LoginController {
    private final LoginView loginView;

    public LoginController() {
        loginView = new LoginView(this::loginAction);
    }

    private void loginAction() {
        loginView.getActionTarget().setFill(Color.FIREBRICK);
        loginView.getActionTarget().setText("Sign in button pressed");

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(loginView.getPasswordField().getText().getBytes(StandardCharsets.UTF_8));
            String hash = new String(hashBytes, StandardCharsets.UTF_8);

            LoginData data = new LoginData(loginView.getNameTextField().getText(), hash);
            URI uri = URI.create("http://127.0.0.1:8080/register");
            HttpClient client = HttpClient.newHttpClient();
            String payload = new ObjectMapper().writeValueAsString(data);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(payload)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status: " + response.statusCode());
        }
        catch (NoSuchAlgorithmException | InterruptedException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
