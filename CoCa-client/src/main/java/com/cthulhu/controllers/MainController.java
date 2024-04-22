package com.cthulhu.controllers;

import com.cthulhu.views.IView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController extends Application {
    private static Scene currentScene;
    private static Stage stage;
    private final LoginController loginController;
    private final RegistrationController registrationController;

    public MainController() {
        loginController = new LoginController();
        registrationController = new RegistrationController();

        loginController.setRegistrationView(registrationController.getView());
        registrationController.setLoginView(loginController.getView());

        currentScene = loginController.getView().getScene();
    }

    @Override
    public void start(Stage stage) {
        MainController.stage = stage;
        stage.setTitle("Login");
        stage.setScene(currentScene);
        stage.show();
    }

    public static void setCurrentScene(IView view) {
        currentScene = view.getScene();
        view.refresh();
        stage.setScene(currentScene);
    }
}
