package com.cthulhu.states;

import com.cthulhu.enums.RollType;
import com.cthulhu.events.RollResultEvent;
import com.cthulhu.services.GeneratorService;

import java.util.ArrayList;
import java.util.List;

public class InitialRoll extends RollState {
    private final List<Integer> diceRolls;
    private final List<RollType> rollTypes;

    public InitialRoll(GeneratorService generatorService) {
        super(generatorService);
        diceRolls = new ArrayList<>();
        rollTypes = new ArrayList<>();
    }

    @Override
    public RollResultEvent roll(String name, String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        switch (bonusDie) {
            case -1 -> {
                if (attributeDie >= skillDie) {
                    var roll = generatorService.rollDie(attributeDie);
                    diceRolls.add(roll);
                    rollTypes.add(RollType.ATTRIBUTE);
                } else {
                    var roll = generatorService.rollDie(skillDie);
                    diceRolls.add(roll);
                    rollTypes.add(RollType.SKILL);
                }
            }
            case 0 -> {
                var attributeRoll = generatorService.rollDie(attributeDie);
                var skillRoll = generatorService.rollDie(skillDie);
                diceRolls.add(attributeRoll);
                diceRolls.add(skillRoll);
                rollTypes.add(RollType.ATTRIBUTE);
                rollTypes.add(RollType.SKILL);
            }
            case 1 -> {
                var min = Math.min(attributeDie, skillDie);
                var attributeRoll = generatorService.rollDie(attributeDie);
                var skillRoll = generatorService.rollDie(skillDie);
                var bonusRoll = generatorService.rollDie(min);
                diceRolls.add(attributeRoll);
                diceRolls.add(skillRoll);
                diceRolls.add(bonusRoll);
                rollTypes.add(RollType.ATTRIBUTE);
                rollTypes.add(RollType.SKILL);
                rollTypes.add(RollType.BONUS);
            }
        }

        int successes = 0;
        for(var roll : diceRolls) {
            if(roll >= 10) {
                successes += 2;
            }
            else if(roll >= 6) {
                successes++;
            }
        }

        return new RollResultEvent(name, attribute, skill, attributeDie, skillDie, diceRolls, rollTypes, successes);
    }
}
