package com.cthulhu.services;

import com.cthulhu.models.Account;
import com.cthulhu.repositories.AccountRepository;
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

    public void save(Account account) {
        repository.save(account);
    }
}
