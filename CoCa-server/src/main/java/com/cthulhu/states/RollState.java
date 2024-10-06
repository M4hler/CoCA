package com.cthulhu.states;

import com.cthulhu.enums.RollType;
import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

import java.util.ArrayList;

public abstract class RollState {
    protected final GeneratorService generatorService;
    protected RollResult rollResult;
    protected final boolean isHuman;
    protected final boolean canForce;

    public RollState(GeneratorService generatorService, RollResult rollResult, boolean isHuman, boolean canForce) {
        this.generatorService = generatorService;
        this.rollResult = rollResult;
        this.isHuman = isHuman;
        this.canForce = canForce;
    }

    public abstract RollState transition();

    public RollResult roll(String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        var diceRolls = new ArrayList<Integer>();
        var rollTypes = new ArrayList<RollType>();

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

        rollResult = new RollResult(attribute, skill, attributeDie, skillDie, diceRolls, rollTypes, successes, canForce);
        return rollResult;
    }
}
