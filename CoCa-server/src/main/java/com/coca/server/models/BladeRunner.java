package com.coca.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Entity
@Getter
@Setter
public class BladeRunner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String nickname;
    private String surname;

    @ManyToOne
    @JoinColumn(name = "account_name")
    @JsonIgnore
    private Account account;
    @OneToOne
    @JoinColumn
    private AvatarImage avatarImage;

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
    private final Map<String, Pair<String, Supplier<Integer>>> skillToAttribute;
    @JsonIgnore
    @Transient
    @Getter(AccessLevel.NONE)
    private final Map<String, Supplier<Integer>> skillToValue;

    public BladeRunner() {
        skillToAttribute = new HashMap<>();
        skillToAttribute.put("force", Pair.of("strength", this::getStrength));
        skillToAttribute.put("handToHandCombat", Pair.of("strength", this::getStrength));
        skillToAttribute.put("stamina", Pair.of("strength", this::getStrength));
        skillToAttribute.put("firearms", Pair.of("agility", this::getAgility));
        skillToAttribute.put("mobility", Pair.of("agility", this::getAgility));
        skillToAttribute.put("stealth", Pair.of("agility", this::getAgility));
        skillToAttribute.put("medicalAid", Pair.of("intelligence", this::getIntelligence));
        skillToAttribute.put("observation", Pair.of("intelligence", this::getIntelligence));
        skillToAttribute.put("tech", Pair.of("intelligence", this::getIntelligence));
        skillToAttribute.put("connections", Pair.of("empathy", this::getEmpathy));
        skillToAttribute.put("manipulation", Pair.of("empathy", this::getEmpathy));
        skillToAttribute.put("insight", Pair.of("empathy", this::getEmpathy));

        skillToValue = new HashMap<>();
        skillToValue.put("strength", this::getStrength);
        skillToValue.put("agility", this::getAgility);
        skillToValue.put("intelligence", this::getIntelligence);
        skillToValue.put("empathy", this::getEmpathy);

        skillToValue.put("force", this::getForce);
        skillToValue.put("handToHandCombat", this::getHandToHandCombat);
        skillToValue.put("stamina", this::getStamina);
        skillToValue.put("firearms", this::getFirearms);
        skillToValue.put("mobility", this::getMobility);
        skillToValue.put("stealth", this::getStealth);
        skillToValue.put("medicalAid", this::getMedicalAid);
        skillToValue.put("observation", this::getObservation);
        skillToValue.put("tech", this::getTech);
        skillToValue.put("connections", this::getConnections);
        skillToValue.put("manipulation", this::getManipulation);
        skillToValue.put("insight", this::getInsight);
    }

    public int getAttributeValueForSkill(String skill) {
        return skillToAttribute.get(skill).getSecond().get();
    }

    public String getAttributeForSkill(String skill) {
        return skillToAttribute.get(skill).getFirst();
    }

    public int getSkillValue(String skill) {
        return skillToValue.get(skill).get();
    }
}
