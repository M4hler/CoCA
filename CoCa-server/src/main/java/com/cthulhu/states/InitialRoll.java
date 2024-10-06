package com.cthulhu.states;

import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

public class InitialRoll extends RollState {

    public InitialRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman, boolean canForce) {
        super(generatorService, rollResult, isHuman, canForce);
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
