package com.cthulhu.controllers;

import com.cthulhu.listeners.MainListener;
import com.cthulhu.models.LoginResponse;
import com.cthulhu.services.HttpService;
import com.cthulhu.views.LoginView;
import com.cthulhu.views.RegistrationView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.*;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.net.HttpURLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Getter
@Setter
public class LoginController extends AbstractController<LoginView> {
    private final MainController mainController;
    private RegistrationView registrationView;
    private final ObjectMapper mapper;

    public LoginController(MainController mainController) {
        this.mainController = mainController;
        this.mapper = new ObjectMapper();
        view = new LoginView(this::login, this::register);
    }

    private void login() {
        var name = view.getNameTextField().getText();
        var password = view.getPasswordField().getText();

        if(name.isEmpty()) {
            setErrorMessage("User name can't be empty");
            return;
        }

        if(password.isEmpty()) {
            setErrorMessage("Password can't be empty");
            return;
        }

        try {
            var response = HttpService.loginRequest(name, password, mainController.getServerAddress());

            if(Objects.equals(response.statusCode(), HttpURLConnection.HTTP_NOT_FOUND)) {
                setErrorMessage("User with name " + name + " not found");
                return;
            }

            if(Objects.equals(response.statusCode(), HttpURLConnection.HTTP_FORBIDDEN)) {
                setErrorMessage("Wrong password");
                return;
            }

            if(Objects.equals(response.statusCode(), HttpURLConnection.HTTP_GONE)) {
                setErrorMessage("No Blade Runner is linked to this account");
                return;
            }

            if(Objects.equals(response.statusCode(), HttpURLConnection.HTTP_NOT_ACCEPTABLE)) {
                setErrorMessage("Admin hasn't started the session yet");
                return;
            }

            if(!Objects.equals(response.statusCode(), HttpURLConnection.HTTP_OK)) {
                setErrorMessage("Server responded with error, code: " + response.statusCode());
                return;
            }

            if(response.body() != null) {
                var responseBody = response.body();
                var body = mapper.readValue(responseBody, LoginResponse.class);
                createQueue(body.getServerQueue(), body.getBrokerUrl(), body.getBrokerUsername(), body.getBrokerPassword());
                mainController.setQueue(body.getClientQueue());
                mainController.transitionControlToSessionController(body.getIsAdmin(), body.getBladeRunner());
            }
            else {
               setErrorMessage("Body was null");
            }
        }
        catch(NoSuchAlgorithmException e) {
            setErrorMessage("Wrong algorithm used to hash the password");
        }
        catch(JMSException e) {
            setErrorMessage("Couldn't create queue");
        }
        catch (JsonProcessingException e) {
            setErrorMessage("Couldn't read message body");
        }
    }

    private void register() {
        mainController.setCurrentScene(registrationView);
    }

    private void setErrorMessage(String message) {
        view.getErrorText().setFill(Color.FIREBRICK);
        view.getErrorText().setText(message);
    }

    public void createQueue(String name, String url, String username, String password) throws JMSException {
        var connectionFactory = new ActiveMQConnectionFactory(url);
        var connection = connectionFactory.createConnection(username, password);
        connection.start();

        var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        var destination = session.createQueue(name);

        var consumer = session.createConsumer(destination);
        var listener = new MainListener();
        consumer.setMessageListener(listener);
    }
}
