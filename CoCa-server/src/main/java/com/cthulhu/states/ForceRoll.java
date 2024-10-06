package com.cthulhu.states;

import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

public class ForceRoll extends RollState {
    public ForceRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman, boolean canForce) {
        super(generatorService, rollResult, isHuman, canForce);
    }

    @Override
    public RollState transition() {
        return new InitialRoll(generatorService, null, isHuman, true);
    }
}
