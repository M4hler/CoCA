package com.coca.server.repositories;

import com.coca.server.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);
    @Query("SELECT salt FROM Account WHERE name = ?1")
    String findSaltByName(String name);
    @Query("SELECT password FROM Account WHERE name = ?1")
    String findPasswordByName(String name);
}
