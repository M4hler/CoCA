package com.cthulhu.controllers;

import com.cthulhu.models.Account;
import com.cthulhu.models.RegistrationData;
import com.cthulhu.services.AccountService;
import org.springframework.http.HttpStatus;
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

    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public HttpStatus register(@RequestBody RegistrationData registrationData) throws NoSuchAlgorithmException {
        if(accountService.userExists(registrationData.getName())) {
            return HttpStatus.CONFLICT;
        }

        String salt = UUID.randomUUID().toString().substring(0, 16);
        String toHash = registrationData.getPassword() + salt;

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(toHash.getBytes());
        byte[] hashedPassword = messageDigest.digest();
        String password = new String(hashedPassword, StandardCharsets.UTF_8).substring(0, 64);

        Account account = new Account(registrationData.getName(), password, salt);
        accountService.save(account);

        return HttpStatus.OK;
    }
}
