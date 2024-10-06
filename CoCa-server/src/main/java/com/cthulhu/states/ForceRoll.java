package com.cthulhu.states;

import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

public class ForceRoll extends RollState {
    public ForceRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman, boolean canPush) {
        super(generatorService, rollResult, isHuman, canPush);
    }

    @Override
    public RollState transition() {
        return new InitialRoll(generatorService, null, isHuman, true);
    }
}
