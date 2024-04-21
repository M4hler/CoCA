package com.cthulhu;

import com.cthulhu.models.LoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("Welcome to CoCa client");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);

        Label userName = new Label("User name:");
        grid.add(userName, 0, 1);

        TextField userNameTextField = new TextField();
        grid.add(userNameTextField, 1, 1);

        Label password = new Label("Password:");
        grid.add(password, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button loginButton = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginButton);
        grid.add(hbBtn, 1, 4);

        Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        loginButton.setOnAction(e -> {
            actionTarget.setFill(Color.FIREBRICK);
            actionTarget.setText("Sign in button pressed");

            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-512");
                byte[] hashBytes = digest.digest(passwordField.getText().getBytes(StandardCharsets.UTF_8));
                String hash = new String(hashBytes, StandardCharsets.UTF_8);

                LoginData data = new LoginData(userNameTextField.getText(), hash);
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
        });

        Scene scene = new Scene(grid, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
