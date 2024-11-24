package com.coca.client.controllers;

import com.coca.client.services.CoCaListenerService;
import com.coca.client.views.IView;
import com.coca.client.models.BladeRunner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

public class MainController extends Application {
    private Scene currentScene;
    private Stage stage;
    @Getter
    private String queue;
    @Getter
    private String serverAddress;
    private final LoginController loginController;

    public MainController() {
        var serverController = new ServerController(this);
        loginController = new LoginController(this);
        var registrationController = new RegistrationController(this);
        var service = new CoCaListenerService();

        loginController.setRegistrationView(registrationController.getView());
        registrationController.setLoginView(loginController.getView());

        currentScene = serverController.getView().getScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("CoCa");
        stage.setScene(currentScene);
        stage.show();
    }

    public void setCurrentScene(IView view) {
        currentScene = view.getScene();
        view.refresh();
        stage.setScene(currentScene);
    }

    public void setQueue(String name) {
        queue = name;
    }

    public void transitionToLoginController(String address) {
        serverAddress = address;
        setCurrentScene(loginController.getView());
    }

    public void transitionControlToSessionController(boolean isAdmin, BladeRunner bladeRunner) {
        var sessionController = new SessionController(this, isAdmin, bladeRunner);
        setCurrentScene(sessionController.getView());
    }
}
