package com.cthulhu.services;

import com.cthulhu.events.RollResultEvent;
import com.cthulhu.states.NoActiveRoll;
import com.cthulhu.states.RollState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RollEventService {
    private final GeneratorService generatorService;
    private final MessageSenderService messageSenderService;
    private final BladeRunnerService bladeRunnerService;
    private final Map<String, RollState> rollStates;

    public RollEventService(GeneratorService generatorService, MessageSenderService messageSenderService, BladeRunnerService bladeRunnerService) {
        this.generatorService = generatorService;
        this.messageSenderService = messageSenderService;
        this.bladeRunnerService = bladeRunnerService;
        rollStates = new HashMap<>();
    }

    public void rollDice(String name, String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        if(!rollStates.containsKey(name)) {
            var isHuman = bladeRunnerService.getBladeRunner(name).isHuman();
            var noActiveRoll = new NoActiveRoll(generatorService, isHuman);
            rollStates.put(name, noActiveRoll);
        }

        var state = rollStates.get(name);
        var result = state.roll(attribute, skill, attributeDie, skillDie, bonusDie);
        rollStates.put(name, state.transition());
        messageSenderService.sendRollResultEvent(new RollResultEvent(name, result));
    }
}
