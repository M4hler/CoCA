package com.cthulhu.controllers;

import com.cthulhu.services.HttpService;
import com.cthulhu.views.ServerView;

import java.util.Objects;

public class ServerController extends AbstractController<ServerView> {
    private final MainController mainController;

    public ServerController(MainController mainController) {
        this.mainController = mainController;
        view = new ServerView(this);
    }

    public void tryConnect(String address) {
        var response = HttpService.healthCheckRequest(address);
        if(Objects.equals(response.statusCode(), 200)) {
            mainController.transitionToLoginController(address);
        }
    }
}
