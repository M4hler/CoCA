package com.cthulhu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BladeRunner {
    @Id
    private String name;
    private boolean isHuman;
    private boolean secretReplicant;
    private String archetype;
    private int yearsOnTheForce;
    private int strength;
    private int agility;
    private int intelligence;
    private int empathy;
    private int health;
    private int resolve;
    private int promotionPoints;
    private int humanityPoints;
    private int chinyenPoints;
}
