package com.cthulhu.states;

import com.cthulhu.events.RollResultEvent;
import com.cthulhu.services.GeneratorService;

public class ForceRoll extends RollState {
    public ForceRoll(GeneratorService generatorService) {
        super(generatorService);
    }

    @Override
    public RollResultEvent roll(String name, String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        return null;
    }
}
