package com.cthulhu.views;

import com.cthulhu.controllers.ServerController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ServerView implements IView {
    private final ServerController controller;
    private final Scene scene;

    public ServerView(ServerController controller) {
        this.controller = controller;

        var grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        var title = new Text("Server address");
        title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(title, 0, 0, 2, 1);

        var address = new TextField();
        grid.add(address, 0, 1);

        var button = new Button("YOLO");
        button.setOnAction(e -> connect(address.getText()));
        grid.add(button, 0, 2);

        scene = new Scene(grid, 800, 600);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void refresh() {

    }

    private void connect(String address) {
        controller.tryConnect(address);
    }
}
