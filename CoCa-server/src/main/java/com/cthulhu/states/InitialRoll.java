package com.cthulhu.states;

import com.cthulhu.enums.RollType;
import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

import java.util.ArrayList;

public class InitialRoll extends RollState {
    public InitialRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman) {
        super(generatorService, rollResult, isHuman);
    }

    @Override
    public RollResult roll() {
        var dice = new ArrayList<Integer>();
        var rollTypes = new ArrayList<RollType>();
        var skippedDice = new ArrayList<Integer>();
        var skippedRollTypes = new ArrayList<RollType>();

        for(int i = 0; i < rollResult.getDiceRolls().size(); i++) {
            var roll = rollResult.getDiceRolls().get(i);
            var type = rollResult.getRollTypes().get(i);
            if(roll == 1) {
                skippedDice.add(roll);
                skippedRollTypes.add(type);
                continue;
            }

            dice.add(roll);
            rollTypes.add(rollResult.getRollTypes().get(i));
        }

        var diceRolls = roll(dice);
        diceRolls.addAll(skippedDice);
        rollTypes.addAll(skippedRollTypes);
        var successes = countSuccesses(diceRolls);

        rollResult = new RollResult(rollResult.getAttribute(), rollResult.getSkill(), rollResult.getAttributeValue(),
                rollResult.getSkillValue(), diceRolls, dice, rollTypes, successes, !isHuman);
        return rollResult;
    }

    @Override
    public RollState transition() {
        if(isHuman) {
            return new InitialRoll(generatorService, null, true);
        }
        else {
            return new PushRoll(generatorService, rollResult, false);
        }
    }
}
