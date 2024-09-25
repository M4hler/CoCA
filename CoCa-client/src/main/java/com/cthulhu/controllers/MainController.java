package com.cthulhu.controllers;

import com.cthulhu.models.Account;
import com.cthulhu.services.CoCaListenerService;
import com.cthulhu.views.IView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

public class MainController extends Application {
    private static Scene currentScene;
    private static Stage stage;
    private final LoginController loginController;
    private final RegistrationController registrationController;
    private Account account;
    @Getter
    private static String queue;

    public MainController() {
        account = new Account();
        loginController = new LoginController(account);
        registrationController = new RegistrationController();
        var service = new CoCaListenerService();

        loginController.setRegistrationView(registrationController.getView());
        registrationController.setLoginView(loginController.getView());

        currentScene = loginController.getView().getScene();
    }

    @Override
    public void start(Stage stage) {
        MainController.stage = stage;
        stage.setTitle("CoCa");
        stage.setScene(currentScene);
        stage.show();
    }

    public static void setCurrentScene(IView view) {
        currentScene = view.getScene();
        view.refresh();
        stage.setScene(currentScene);
    }

    public static void setQueue(String name) {
        queue = name;
    }
}
