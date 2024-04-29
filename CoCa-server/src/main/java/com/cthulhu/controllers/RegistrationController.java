package com.cthulhu.controllers;

import com.cthulhu.listeners.MainListener;
import com.cthulhu.models.Account;
import com.cthulhu.models.LoginData;
import com.cthulhu.services.AccountService;
import jakarta.jms.*;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
public class RegistrationController {
    private final AccountService accountService;
    private final ConnectionFactory connectionFactory;
    private final MessageDigest messageDigest;
    private final JmsTemplate jmsTemplate;

    public RegistrationController(AccountService accountService, ConnectionFactory connectionFactory,
                                  JmsTemplate jmsTemplate) throws NoSuchAlgorithmException {
        this.accountService = accountService;
        this.connectionFactory = connectionFactory;
        this.jmsTemplate = jmsTemplate;
        messageDigest = MessageDigest.getInstance("SHA-512");
    }

    @PostMapping("/login")
    public HttpStatus login(@RequestBody LoginData loginData) throws JMSException {
        if(!accountService.userExists(loginData.getName())) {
            return HttpStatus.NOT_FOUND;
        }

        String salt = accountService.getSalt(loginData.getName());
        String password = getPassword(loginData.getPassword(), salt);
        String dbPassword = accountService.getPassword(loginData.getName());

        if(!dbPassword.equals(password)) {
            return HttpStatus.FORBIDDEN;
        }

        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(loginData.getName());
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MainListener());

        return HttpStatus.OK;
    }

    @PostMapping("/register")
    public HttpStatus register(@RequestBody LoginData loginData) {
        if(accountService.userExists(loginData.getName())) {
            return HttpStatus.CONFLICT;
        }

        String salt = UUID.randomUUID().toString().substring(0, 16);
        String password = getPassword(loginData.getPassword(), salt);

        Account account = new Account(loginData.getName(), password, salt, false);
        accountService.save(account);

        return HttpStatus.OK;
    }

    @GetMapping("/send")
    public HttpStatus send() {
        jmsTemplate.convertAndSend("testQueue", "Hello World!");
        return HttpStatus.OK;
    }

    private String getPassword(String password, String salt) {
        String toHash = password + salt;
        messageDigest.update(toHash.getBytes());
        byte[] hash = messageDigest.digest();
        String result = new String(hash, StandardCharsets.UTF_8);
        return result.substring(0, Math.min(result.length(), 64));
    }
}
