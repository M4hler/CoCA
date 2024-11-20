package com.cthulhu.controllers;

import com.cthulhu.services.HttpService;
import com.cthulhu.views.LoginView;
import com.cthulhu.views.RegistrationView;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Getter
@Setter
public class RegistrationController extends AbstractController<RegistrationView> {
    private final MainController mainController;
    private LoginView loginView;

    public RegistrationController(MainController mainController) {
        this.mainController = mainController;
        view = new RegistrationView(this::registerAction, this::backAction);
    }

    private void registerAction() {
        var name = view.getNameTextField().getText();
        var password = view.getPasswordField().getText();
        var passwordRepeat = view.getPasswordRepeatField().getText();

        if(name.length() < 4) {
            setErrorMessage("Name must be at least 4 characters long");
            return;
        }

        if(password.length() < 8) {
            setErrorMessage("Password must be at least 8 characters long");
            return;
        }

        if(!password.equals(passwordRepeat)) {
            setErrorMessage("Repeated password doesn't match the original one");
            return;
        }

        try {
            var response = HttpService.registerRequest(name, password, mainController.getServerAddress());

            if(Objects.equals(response.statusCode(), 409)) {
                setErrorMessage("User with this name already exists");
                return;
            }

            if(!Objects.equals(response.statusCode(), 200)) {
                setErrorMessage("Server responded with error, code: " + response.statusCode());
                return;
            }

            backAction();
        }
        catch(NoSuchAlgorithmException e) {
            setErrorMessage("Wrong algorithm used to hash the password");
        }
    }

    private void backAction() {
        mainController.setCurrentScene(loginView);
    }

    private void setErrorMessage(String message) {
        view.getErrorText().setFill(Color.FIREBRICK);
        view.getErrorText().setText(message);
    }
}
