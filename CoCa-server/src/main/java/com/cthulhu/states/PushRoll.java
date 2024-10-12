package com.cthulhu.states;

import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

public class PushRoll extends RollState {
    public PushRoll(GeneratorService generatorService, RollResult rollResult, boolean isHuman) {
        super(generatorService, rollResult, isHuman);
    }

    @Override
    public RollResult roll() {
        return rollResult;
    }

    @Override
    public RollState transition() {
        return null;
    }
}
