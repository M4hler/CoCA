package com.cthulhu.states;

import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

public class InitialRoll extends RollState {

    public InitialRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman, boolean canPush) {
        super(generatorService, rollResult, isHuman, canPush);
    }

    @Override
    public RollState transition() {
        if(isHuman) {
            return new InitialRoll(generatorService, null, true, true);
        }
        else {
            return new ForceRoll(generatorService, rollResult, false, false);
        }
    }
}
