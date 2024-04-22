package com.cthulhu;

import com.cthulhu.controllers.RegistrationController;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Login");

        var controller = new RegistrationController();
        Scene scene = new Scene(controller.getRegistrationView().getGrid(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
