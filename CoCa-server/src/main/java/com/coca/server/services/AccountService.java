package com.coca.server.services;

import com.coca.server.repositories.AccountRepository;
import com.coca.server.models.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public boolean userExists(String name) {
        return repository.findByName(name) != null;
    }

    public Account getAccount(String name) {
        return repository.findByName(name);
    }

    public String getSalt(String name) {
        return repository.findSaltByName(name);
    }

    public String getPassword(String name) {
        return repository.findPasswordByName(name);
    }

    public void save(Account account) {
        repository.save(account);
    }
}
