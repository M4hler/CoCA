package com.cthulhu.controllers;

import com.cthulhu.models.Account;
import com.cthulhu.models.LoginData;
import com.cthulhu.models.LoginResponse;
import com.cthulhu.services.AccountService;
import com.cthulhu.services.MessageSender;
import jakarta.jms.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final AccountService accountService;
    private final MessageSender messageSender;
    private final MessageDigest messageDigest;
    private final JmsTemplate jmsTemplate;

    public RestController(AccountService accountService, MessageSender messageSender,
                          JmsTemplate jmsTemplate) throws NoSuchAlgorithmException {
        this.accountService = accountService;
        this.messageSender = messageSender;
        this.jmsTemplate = jmsTemplate;
        messageDigest = MessageDigest.getInstance("SHA-512");
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponse> login(@RequestBody LoginData loginData) {
        if(!accountService.userExists(loginData.getName())) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Account account = accountService.getAccount(loginData.getName());
        String salt = accountService.getSalt(loginData.getName());
        String password = getPassword(loginData.getPassword(), salt);
        String dbPassword = accountService.getPassword(loginData.getName());

        if(!dbPassword.equals(password)) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        try {
            String queue = messageSender.createQueue(loginData.getName());
            var isAdmin = accountService.isAdmin(loginData.getName());
            var bladeRunner = account.getBladeRunners().get(0);
            var body = new LoginResponse(queue, isAdmin, bladeRunner);
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        catch(JMSException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginData loginData) {
        if(accountService.userExists(loginData.getName())) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        String salt = UUID.randomUUID().toString().substring(0, 16);
        String password = getPassword(loginData.getPassword(), salt);

        Account account = new Account(loginData.getName(), password, salt, false, null);
        accountService.save(account);

        return new ResponseEntity<>(null, HttpStatus.OK);
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
