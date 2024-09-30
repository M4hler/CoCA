package com.cthulhu.states;

import com.cthulhu.events.Event;
import com.cthulhu.services.GeneratorService;

public abstract class RollState {
    protected GeneratorService generatorService;

    public RollState(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public abstract Event roll(String name, String attribute, String skill, int attributeDie, int skillDie, int bonusDie);
}
