package com.coca.client.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@Setter
public class BladeRunner {
    private String name;
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

    @Getter(AccessLevel.NONE)
    private final Map<String, Supplier<Integer>> skillToValue;
    @Getter(AccessLevel.NONE)
    private final Map<String, Supplier<Integer>> currentStatusToValue;

    public BladeRunner() {
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

        currentStatusToValue = new HashMap<>();
        currentStatusToValue.put("health", this::getHealth);
        currentStatusToValue.put("resolve", this::getResolve);
        currentStatusToValue.put("promotionPoints", this::getPromotionPoints);
        currentStatusToValue.put("humanityPoints", this::getHumanityPoints);
        currentStatusToValue.put("chinyenPoints", this::getChinyenPoints);
    }

    public int getSkillValue(String skill) {
        return skillToValue.get(skill).get();
    }

    public int getCurrentStatusValue(String status) {
        return currentStatusToValue.get(status).get();
    }
}
