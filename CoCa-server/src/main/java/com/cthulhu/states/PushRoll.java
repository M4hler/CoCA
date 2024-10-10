package com.cthulhu.states;

import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

public class PushRoll extends RollState {
    public PushRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman, boolean canPush) {
        super(generatorService, rollResult, isHuman, canPush);
    }

    @Override
    public RollResult roll() {
        return null;
    }

    @Override
    public RollState transition() {
        return new InitialRoll(generatorService, null, isHuman, true);
    }
}
