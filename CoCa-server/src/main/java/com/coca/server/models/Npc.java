package com.coca.server.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Npc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String nickname;
    private String surname;

    private boolean isHuman;

    private int strength;
    private int agility;
    private int intelligence;
    private int empathy;

    private int health;
    private int resolve;

    private int force;
    private int handToHandCombat;
    private int stamina;
    private int firearms;
    private int mobility;
    private int stealth;
    private int medicalAid;
    private int observation;
    private int tech;
    private int connections;
    private int manipulation;
    private int insight;
}
