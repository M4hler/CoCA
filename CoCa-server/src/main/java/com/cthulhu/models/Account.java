package com.cthulhu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Account {
    @Id
    private String name;
    private String password;
    private String salt;
}
