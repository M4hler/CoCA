package com.cthulhu.controllers;

import com.cthulhu.models.Account;
import com.cthulhu.models.RegistrationData;
import com.cthulhu.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RegistrationController {
    private final AccountService accountService;

    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public HttpStatus register(@RequestBody RegistrationData registrationData) {
        System.out.format("Received %s %s\n", registrationData.getName(), registrationData.getPassword());

        if(accountService.userExists(registrationData.getName())) {
            return HttpStatus.CONFLICT;
        }

        Account account = new Account(registrationData.getName(), registrationData.getPassword(), UUID.randomUUID().toString().substring(0, 16));
        accountService.save(account);

        return HttpStatus.OK;
    }
}
