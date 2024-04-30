package com.cthulhu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private String name;
    private String password;
    private String salt;
    private boolean isAdmin;
    @OneToMany(mappedBy = "account")
    private List<BladeRunner> bladeRunners;
}
