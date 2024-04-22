package com.cthulhu.controllers;

import com.cthulhu.services.HttpService;
import com.cthulhu.views.LoginView;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Getter
public class LoginController {
    private final LoginView loginView;

    public LoginController() {
        loginView = new LoginView(this::login, this::register);
    }

    private void login() {
        String name = loginView.getNameTextField().getText();
        String password = loginView.getPasswordField().getText();

        if(name.isEmpty()) {
            setErrorMessage("User name can't be empty");
            return;
        }

        if(password.isEmpty()) {
            setErrorMessage("Password can't be empty");
            return;
        }

        try {
            HttpStatus status = HttpService.loginRequest(name, password);

            if(Objects.equals(status, HttpStatus.NOT_FOUND)) {
                setErrorMessage("User with name " + name + " not found");
                return;
            }

            if(Objects.equals(status, HttpStatus.FORBIDDEN)) {
                setErrorMessage("Wrong password");
                return;
            }

            if(!Objects.equals(status, HttpStatus.OK)) {
                setErrorMessage("Server responded with error, code: " + status.value());
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
        catch(IOException e) {
            setErrorMessage("Server isn't responding");
        }
    }

    private void register() {

    }

    private void setErrorMessage(String message) {
        loginView.getErrorText().setFill(Color.FIREBRICK);
        loginView.getErrorText().setText(message);
    }
}
