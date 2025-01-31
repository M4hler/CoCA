package com.coca.server.services;

import com.coca.server.events.RollResultEvent;
import com.coca.server.states.NoActiveRoll;
import com.coca.server.states.RollState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RollEventService {
    private final GeneratorService generatorService;
    private final MessageSenderService messageSenderService;
    private final BladeRunnerService bladeRunnerService;
    private final Map<Integer, RollState> rollStates;

    public RollEventService(GeneratorService generatorService, MessageSenderService messageSenderService, BladeRunnerService bladeRunnerService) {
        this.generatorService = generatorService;
        this.messageSenderService = messageSenderService;
        this.bladeRunnerService = bladeRunnerService;
        rollStates = new HashMap<>();
    }

    public void rollDice(int id, String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        if(!rollStates.containsKey(id) || rollStates.get(id) == null) {
            var isHuman = bladeRunnerService.getBladeRunner(id).isHuman();
            var noActiveRoll = new NoActiveRoll(generatorService, isHuman, attribute, skill, attributeDie, skillDie, bonusDie);
            rollStates.put(id, noActiveRoll);
        }

        var state = rollStates.get(id);
        var result = state.roll();
        rollStates.put(id, state.transition());
        messageSenderService.sendToAll(new RollResultEvent(id, result));
    }

    public void pushRoll(int id) {
        var state = rollStates.get(id);
        var result = state.roll();
        rollStates.put(id, state.transition());
        messageSenderService.sendToAll(new RollResultEvent(id, result));
    }

    public void accept(int id) {
        rollStates.put(id, null);
    }
}
