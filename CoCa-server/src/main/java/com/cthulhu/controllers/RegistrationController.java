package com.cthulhu.controllers;

import com.cthulhu.models.Account;
import com.cthulhu.models.LoginData;
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
    private final MessageDigest messageDigest;

    public RegistrationController(AccountService accountService) throws NoSuchAlgorithmException {
        this.accountService = accountService;
        messageDigest = MessageDigest.getInstance("SHA-512");
    }

    @PostMapping("/login")
    public HttpStatus login(@RequestBody LoginData loginData) {
        if(!accountService.userExists(loginData.getName())) {
            return HttpStatus.NOT_FOUND;
        }

        String salt = accountService.getSalt(loginData.getName());
        String password = getPassword(loginData.getPassword(), salt);
        String dbPassword = accountService.getPassword(loginData.getName());

        if(!dbPassword.equals(password)) {
            return HttpStatus.FORBIDDEN;
        }

        return HttpStatus.OK;
    }

    @PostMapping("/register")
    public HttpStatus register(@RequestBody RegistrationData registrationData) {
        if(accountService.userExists(registrationData.getName())) {
            return HttpStatus.CONFLICT;
        }

        String salt = UUID.randomUUID().toString().substring(0, 16);
        String password = getPassword(registrationData.getPassword(), salt);

        Account account = new Account(registrationData.getName(), password, salt);
        accountService.save(account);

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
