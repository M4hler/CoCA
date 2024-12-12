package com.coca.client.controllers;

import com.coca.client.views.ServerView;
import com.coca.client.services.HttpService;

import java.net.HttpURLConnection;
import java.util.Objects;

public class ServerController extends AbstractController<ServerView> {
    private final MainController mainController;

    public ServerController(MainController mainController) {
        this.mainController = mainController;
        view = new ServerView(this);
    }

    public void tryConnect(String address) {
        var response = HttpService.healthCheckRequest(address);
        if (Objects.equals(response.statusCode(), HttpURLConnection.HTTP_OK)) {
            mainController.transitionToLoginController(address);
        } else {
            System.out.println("Response: " + response.statusCode());
        }
    }
}
