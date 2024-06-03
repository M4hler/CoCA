package com.cthulhu.controllers;

import com.cthulhu.models.Account;
import com.cthulhu.models.BladeRunner;
import com.cthulhu.models.LoginData;
import com.cthulhu.models.LoginResponse;
import com.cthulhu.services.AccountService;
import com.cthulhu.services.MessageSenderService;
import jakarta.jms.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }

        if(!account.isAdmin() && !messageSenderService.isAdminOnline()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            String queue = messageSenderService.createQueue(account);
            BladeRunner bladeRunner = null;
            if(!account.isAdmin()) {
                bladeRunner = account.getBladeRunners().get(0);
            }

            var body = new LoginResponse(queue, account.isAdmin(), bladeRunner);
            messageSenderService.sendJoinEvent(loginData.getName(), bladeRunner);
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
