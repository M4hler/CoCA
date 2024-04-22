package com.cthulhu.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.Getter;

@Getter
public class LoginView extends Parent {
    private final TextField nameTextField;
    private final PasswordField passwordField;
    private final Text errorText;
    private final GridPane grid;

    public LoginView(Runnable loginAction, Runnable registerAction) {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text title = new Text("Welcome to CoCa client");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);

        Label userName = new Label("User name:");
        grid.add(userName, 0, 1);

        nameTextField = new TextField();
        grid.add(nameTextField, 1, 1);

        Label password = new Label("Password:");
        grid.add(password, 0, 2);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button registerButton = new Button("Register");
        HBox registerBox = new HBox(10);
        registerBox.setAlignment(Pos.BOTTOM_RIGHT);
        registerBox.getChildren().add(registerButton);
        grid.add(registerBox, 0, 3);

        Button loginButton = new Button("Login");
        HBox loginBox = new HBox(10);
        loginBox.setAlignment(Pos.BOTTOM_RIGHT);
        loginBox.getChildren().add(loginButton);
        grid.add(loginBox, 1, 3);

        errorText = new Text();
        grid.add(errorText, 1, 5);

        loginButton.setOnAction(e -> loginAction.run());
        registerButton.setOnAction(e -> registerAction.run());
    }
}
