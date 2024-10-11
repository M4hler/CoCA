package com.cthulhu.states;

import com.cthulhu.enums.RollType;
import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

import java.util.ArrayList;
import java.util.List;

public class NoActiveRoll extends RollState {
    private final String attribute;
    private final String skill;
    private final int attributeDie;
    private final int skillDie;
    private final int bonusDie;

    public NoActiveRoll(GeneratorService generatorService, boolean isHuman, String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        super(generatorService, null, isHuman);
        this.attribute = attribute;
        this.skill = skill;
        this.attributeDie = attributeDie;
        this.skillDie = skillDie;
        this.bonusDie = bonusDie;
    }

    @Override
    public RollResult roll() {
        var dice = new ArrayList<Integer>();
        var rollTypes = new ArrayList<RollType>();

        switch(bonusDie) {
            case -1 -> {
                if (attributeDie >= skillDie) {
                    dice.add(attributeDie);
                    rollTypes.add(RollType.ATTRIBUTE);
                }
                else {
                    dice.add(skillDie);
                    rollTypes.add(RollType.SKILL);
                }
            }
            case 0 -> {
                dice.addAll(List.of(attributeDie, skillDie));
                rollTypes.addAll(List.of(RollType.ATTRIBUTE, RollType.SKILL));
            }
            case 1 -> {
                var min = Math.min(attributeDie, skillDie);
                dice.addAll(List.of(attributeDie, skillDie, min));
                rollTypes.addAll(List.of(RollType.ATTRIBUTE, RollType.SKILL, RollType.BONUS));
            }
        }

        var diceRolls = roll(dice);
        int successes = countSuccesses(diceRolls);

        rollResult = new RollResult(attribute, skill, attributeDie, skillDie, diceRolls, dice, rollTypes, successes,true);
        return rollResult;
    }

    @Override
    public RollState transition() {
        return new InitialRoll(generatorService, rollResult, isHuman);
    }
}
