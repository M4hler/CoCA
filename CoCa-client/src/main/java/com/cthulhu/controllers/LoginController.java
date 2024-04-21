package com.cthulhu.controllers;

import com.cthulhu.models.LoginData;
import com.cthulhu.views.LoginView;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Getter
public class LoginController {
    private final LoginView loginView;

    public LoginController() {
        loginView = new LoginView(this::loginAction);
    }

    private void loginAction() {
        String name = loginView.getNameTextField().getText();
        String password = loginView.getPasswordField().getText();

        if(name.length() < 4) {
            setErrorMessage("Name must be at least 4 characters long");
            return;
        }

        if(password.length() < 8) {
            setErrorMessage("Password must be at least 8 characters long");
            return;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String hash = new String(hashBytes, StandardCharsets.UTF_8);

            LoginData data = new LoginData(name, hash);
            URI uri = URI.create("http://127.0.0.1:8080/register");
            HttpClient client = HttpClient.newHttpClient();
            String payload = new ObjectMapper().writeValueAsString(data);
            HttpRequest request = HttpRequest.newBuilder().uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String status = response.body().replaceAll("\"", "");

            if(Objects.equals(status, HttpStatus.CONFLICT.name())) {
                setErrorMessage("User with this name already exists");
                return;
            }

            if(!Objects.equals(status, HttpStatus.OK.name())) {
                setErrorMessage("Server responded with error, code: " + response.statusCode());
                return;
            }

            setErrorMessage("Everything good");
        }
        catch(NoSuchAlgorithmException e) {
            setErrorMessage("Wrong algorithm used to hash the password");
        }
        catch(InterruptedException e) {
            setErrorMessage("Communication with server was interrupted");
        }
        catch(IOException ex) {
            setErrorMessage("Server isn't responding");
        }
    }

    private void setErrorMessage(String message) {
        loginView.getActionTarget().setFill(Color.FIREBRICK);
        loginView.getActionTarget().setText(message);
    }
}
