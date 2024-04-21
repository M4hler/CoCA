package com.cthulhu.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import lombok.Setter;

@Getter
@Setter
public class LoginView extends Parent {
    private final TextField nameTextField;
    private final PasswordField passwordField;
    private final Text actionTarget;
    private final GridPane grid;

    public LoginView(Runnable loginAction) {
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

        Button loginButton = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginButton);
        grid.add(hbBtn, 1, 4);

        actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        loginButton.setOnAction(e -> loginAction.run());
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
