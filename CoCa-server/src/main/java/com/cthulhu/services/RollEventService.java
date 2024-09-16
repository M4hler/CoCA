package com.cthulhu.services;

import org.springframework.stereotype.Service;

@Service
public class RollEventService {
    private final GeneratorService generatorService;

    public RollEventService(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public void rollDice(int attributeDie, int skillDie, int bonusDie) {
        switch (bonusDie) {
            case -1 -> {
                int max = Math.max(attributeDie, skillDie);
                int roll = generatorService.rollDie(max);
                System.out.println("Roll result: " + roll);
            }
            case 0 -> {
                int attributeRoll = generatorService.rollDie(attributeDie);
                int skillRoll = generatorService.rollDie(skillDie);
                System.out.println("Roll result: " + attributeRoll + " " + skillRoll);
            }
            case 1 -> {
                int min = Math.min(attributeDie, skillDie);
                int attributeRoll = generatorService.rollDie(attributeDie);
                int skillRoll = generatorService.rollDie(skillDie);
                int bonusRoll = generatorService.rollDie(min);
                System.out.println("Roll result: " + attributeRoll + " " + skillRoll + " " + bonusRoll);
            }
        }
    }
}
