package com.cthulhu;

import com.cthulhu.controllers.LoginController;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Login");

        var controller = new LoginController();
        Scene scene = new Scene(controller.getLoginView().getGrid(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
