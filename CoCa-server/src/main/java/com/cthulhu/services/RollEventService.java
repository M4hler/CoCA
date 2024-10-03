package com.cthulhu.services;

import com.cthulhu.states.InitialRoll;
import com.cthulhu.states.RollState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RollEventService {
    private final GeneratorService generatorService;
    private final MessageSenderService messageSenderService;
    private final Map<String, RollState> rollStates;

    public RollEventService(GeneratorService generatorService, MessageSenderService messageSenderService) {
        this.generatorService = generatorService;
        this.messageSenderService = messageSenderService;
        rollStates = new HashMap<>();
    }

    public void rollDice(String name, String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        if(!rollStates.containsKey(name)) {
            var initialRoll = new InitialRoll(generatorService);
            rollStates.put(name, initialRoll);
        }

        var result = rollStates.get(name).roll(name, attribute, skill, attributeDie, skillDie, bonusDie);
        messageSenderService.sendRollResultEvent(result);
    }
}
