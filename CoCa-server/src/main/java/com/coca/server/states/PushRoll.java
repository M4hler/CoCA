package com.coca.server.states;

import com.coca.server.enums.RollType;
import com.coca.server.models.RollResult;
import com.coca.server.services.GeneratorService;

import java.util.List;

public class PushRoll extends InitialRoll {
    public PushRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman) {
        super(generatorService, rollResult, isHuman);
    }

    @Override
    public RollState transition() {
        return null;
    }

    @Override
    protected RollResult createResult(List<Integer> diceRolls, List<Integer> dice, List<RollType> rollTypes, int successes) {
        return new RollResult(rollResult.getAttribute(), rollResult.getSkill(), rollResult.getAttributeValue(),
                rollResult.getSkillValue(), diceRolls, dice, rollTypes, successes, false);
    }
}
