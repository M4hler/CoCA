package com.coca.server.controllers;

import com.coca.server.events.BladeRunnerDataEvent;
import com.coca.server.events.JoinEvent;
import com.coca.server.models.Account;
import com.coca.server.services.AccountService;
import com.coca.server.models.BladeRunner;
import com.coca.server.models.LoginData;
import com.coca.server.models.LoginResponse;
import com.coca.server.services.MessageSenderService;
import jakarta.jms.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final MessageSenderService messageSenderService;
    private final MessageDigest messageDigest;

    public RestController(AccountService accountService,
                          MessageSenderService messageSenderService) throws NoSuchAlgorithmException {
        this.accountService = accountService;
        this.messageSenderService = messageSenderService;
        messageDigest = MessageDigest.getInstance("SHA-512");
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>(null, HttpStatus.OK);
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

        if(!account.isAdmin() && account.getBladeRunners().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.GONE);
        }

        if(!account.isAdmin() && !messageSenderService.isAdminOnline()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            String serverQueue = messageSenderService.createServerQueue(account);
            String playerQueue = messageSenderService.createPlayerQueue(account);
            BladeRunner bladeRunner = null;
            if(!account.isAdmin()) {
                bladeRunner = account.getBladeRunners().get(0);
            }

            var body = new LoginResponse(serverQueue, playerQueue, "tcp://localhost:61616", "artemis", "artemis", account.isAdmin(), bladeRunner);
            messageSenderService.sendToAll(new JoinEvent(loginData.getName()));
            messageSenderService.sendToAdminQueue(new BladeRunnerDataEvent(bladeRunner));
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

    private String getPassword(String password, String salt) {
        String toHash = password + salt;
        messageDigest.update(toHash.getBytes());
        byte[] hash = messageDigest.digest();
        String result = new String(hash, StandardCharsets.UTF_8);
        return result.substring(0, Math.min(result.length(), 64));
    }
}
