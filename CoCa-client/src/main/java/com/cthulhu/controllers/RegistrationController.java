package com.cthulhu.controllers;

import com.cthulhu.services.HttpService;
import com.cthulhu.views.RegistrationView;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Getter
public class RegistrationController {
    private final RegistrationView registrationView;

    public RegistrationController() {
        registrationView = new RegistrationView(this::registerAction);
    }

    private void registerAction() {
        String name = registrationView.getNameTextField().getText();
        String password = registrationView.getPasswordField().getText();
        String passwordRepeat = registrationView.getPasswordRepeatField().getText();

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
            HttpStatus status = HttpService.loginRequest(name, password);

            if(Objects.equals(status, HttpStatus.CONFLICT)) {
                setErrorMessage("User with this name already exists");
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
        catch(IOException ex) {
            setErrorMessage("Server isn't responding");
        }
    }

    private void setErrorMessage(String message) {
        registrationView.getActionTarget().setFill(Color.FIREBRICK);
        registrationView.getActionTarget().setText(message);
    }
}
