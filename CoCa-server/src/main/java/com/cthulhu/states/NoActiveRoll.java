package com.cthulhu.states;

import com.cthulhu.services.GeneratorService;

public class NoActiveRoll extends RollState {
    public NoActiveRoll(GeneratorService generatorService, boolean isHuman) {
        super(generatorService, null, isHuman, true);
    }

    @Override
    public RollState transition() {
        return new InitialRoll(generatorService, rollResult, isHuman, true);
    }
}
