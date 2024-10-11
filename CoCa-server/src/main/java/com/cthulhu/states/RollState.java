package com.cthulhu.states;

import com.cthulhu.models.RollResult;
import com.cthulhu.services.GeneratorService;

import java.util.ArrayList;
import java.util.List;

public abstract class RollState {
    protected final GeneratorService generatorService;
    protected RollResult rollResult;
    protected final boolean isHuman;

    public RollState(GeneratorService generatorService, RollResult rollResult, boolean isHuman) {
        this.generatorService = generatorService;
        this.rollResult = rollResult;
        this.isHuman = isHuman;
    }

    public abstract RollState transition();
    public abstract RollResult roll();

    protected List<Integer> roll(List<Integer> dice) {
        var rolls = new ArrayList<Integer>();
        for(var die : dice) {
            var roll = generatorService.rollDie(die);
            rolls.add(roll);
        }
        return rolls;
    }

    protected int countSuccesses(List<Integer> rolls) {
        int successes = 0;
        for(var roll : rolls) {
            if(roll >= 10) {
                successes += 2;
            }
            else if(roll >= 6) {
                successes++;
            }
        }
        return successes;
    }
}
