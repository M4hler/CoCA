package com.cthulhu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Getter(AccessLevel.NONE)
    private final Map<String, Supplier<Integer>> skillsToAttribute;
    @JsonIgnore
    @Transient
    @Getter(AccessLevel.NONE)
    private final Map<String, Supplier<Integer>> skillsValues;

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

        skillsValues = new HashMap<>();
        skillsValues.put("strength", this::getStrength);
        skillsValues.put("agility", this::getAgility);
        skillsValues.put("intelligence", this::getIntelligence);
        skillsValues.put("empathy", this::getEmpathy);

        skillsValues.put("force", this::getForce);
        skillsValues.put("handToHandCombat", this::getHandToHandCombat);
        skillsValues.put("stamina", this::getStamina);
        skillsValues.put("firearms", this::getFirearms);
        skillsValues.put("mobility", this::getMobility);
        skillsValues.put("stealth", this::getStealth);
        skillsValues.put("medicalAid", this::getMedicalAid);
        skillsValues.put("observation", this::getObservation);
        skillsValues.put("tech", this::getTech);
        skillsValues.put("connections", this::getConnections);
        skillsValues.put("manipulation", this::getManipulation);
        skillsValues.put("insight", this::getInsight);
    }

    public int getAttributeValueForSkill(String skill) {
        return skillsToAttribute.get(skill).get();
    }

    public int getSkillValue(String skill) {
        return skillsValues.get(skill).get();
    }
}
