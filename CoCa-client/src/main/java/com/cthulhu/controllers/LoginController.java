package com.cthulhu.controllers;

import com.cthulhu.models.Account;
import com.cthulhu.services.HttpService;
import com.cthulhu.views.LoginView;
import com.cthulhu.views.RegistrationView;
import jakarta.jms.*;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.http.HttpStatus;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Getter
@Setter
public class LoginController extends AbstractController<LoginView> {
    private RegistrationView registrationView;
    private Account account;

    public LoginController(Account account) {
        this.account = account;
        view = new LoginView(this::login, this::register);
    }

    private void login() {
        String name = view.getNameTextField().getText();
        String password = view.getPasswordField().getText();

        if(name.isEmpty()) {
            setErrorMessage("User name can't be empty");
            return;
        }

        if(password.isEmpty()) {
            setErrorMessage("Password can't be empty");
            return;
        }

        try {
            var response = HttpService.loginRequest(name, password);

            if(Objects.equals(response.getStatusCode(), HttpStatus.NOT_FOUND)) {
                setErrorMessage("User with name " + name + " not found");
                return;
            }

            if(Objects.equals(response.getStatusCode(), HttpStatus.FORBIDDEN)) {
                setErrorMessage("Wrong password");
                return;
            }

            if(!Objects.equals(response.getStatusCode(), HttpStatus.OK)) {
                setErrorMessage("Server responded with error, code: " + response.getStatusCode().value());
                return;
            }

            if(response.getBody() != null) {
                account.setAdmin(response.getBody().getIsAdmin());
                createQueue(response.getBody().getQueue());

                var sessionController = new SessionController(account.isAdmin(), response.getBody().getBladeRunner());
                MainController.setCurrentScene(sessionController.getView());
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
    }

    private void register() {
        MainController.setCurrentScene(registrationView);
    }

    private void setErrorMessage(String message) {
        view.getErrorText().setFill(Color.FIREBRICK);
        view.getErrorText().setText(message);
    }

    public void createQueue(String name) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(name);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new TestListener());
    }
}
