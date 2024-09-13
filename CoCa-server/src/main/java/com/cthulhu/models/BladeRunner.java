package com.cthulhu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Entity
@Getter
@Setter
public class BladeRunner {
    @Id
    private String name;
    @ManyToOne
    @JoinColumn(name = "account_name")
    @JsonIgnore
    private Account account;

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

    private int promotionPoints;
    private int humanityPoints;
    private int chinyenPoints;

    @JsonIgnore
    @Transient
    private final Map<String, Supplier<Integer>> skillsToAttribute;

    public BladeRunner() {
        skillsToAttribute = new HashMap<>();
        skillsToAttribute.put("force", this::getStrength);
        skillsToAttribute.put("handToHandCombat", this::getStrength);
        skillsToAttribute.put("stamina", this::getStrength);
        skillsToAttribute.put("firearms", this::getAgility);
        skillsToAttribute.put("mobility", this::getAgility);
        skillsToAttribute.put("stealth", this::getAgility);
        skillsToAttribute.put("medicalAid", this::getIntelligence);
        skillsToAttribute.put("observation", this::getIntelligence);
        skillsToAttribute.put("tech", this::getIntelligence);
        skillsToAttribute.put("connections", this::getEmpathy);
        skillsToAttribute.put("manipulation", this::getEmpathy);
        skillsToAttribute.put("insight", this::getEmpathy);
    }
}
