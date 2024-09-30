package com.cthulhu.services;

import com.cthulhu.events.RollResultEvent;
import com.cthulhu.enums.RollType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RollEventService {
    private final GeneratorService generatorService;
    private final MessageSenderService messageSenderService;

    public RollEventService(GeneratorService generatorService, MessageSenderService messageSenderService) {
        this.generatorService = generatorService;
        this.messageSenderService = messageSenderService;
    }

    public void rollDice(String name, String attribute, String skill, int attributeDie, int skillDie, int bonusDie) {
        var diceRolls = new ArrayList<Integer>();
        var rollTypes = new ArrayList<RollType>();
        int successes = 0;
        switch (bonusDie) {
            case -1 -> {
                if(attributeDie >= skillDie) {
                    var roll = generatorService.rollDie(attributeDie);
                    diceRolls.add(roll);
                    rollTypes.add(RollType.ATTRIBUTE);
                }
                else {
                    var roll = generatorService.rollDie(skillDie);
                    diceRolls.add(roll);
                    rollTypes.add(RollType.SKILL);
                }
            }
            case 0 -> {
                var attributeRoll = generatorService.rollDie(attributeDie);
                var skillRoll = generatorService.rollDie(skillDie);
                diceRolls.add(attributeRoll);
                diceRolls.add(skillRoll);
                rollTypes.add(RollType.ATTRIBUTE);
                rollTypes.add(RollType.SKILL);
            }
            case 1 -> {
                var min = Math.min(attributeDie, skillDie);
                var attributeRoll = generatorService.rollDie(attributeDie);
                var skillRoll = generatorService.rollDie(skillDie);
                var bonusRoll = generatorService.rollDie(min);
                diceRolls.add(attributeRoll);
                diceRolls.add(skillRoll);
                diceRolls.add(bonusRoll);
                rollTypes.add(RollType.ATTRIBUTE);
                rollTypes.add(RollType.SKILL);
                rollTypes.add(RollType.BONUS);
            }
        }

        for(var roll : diceRolls) {
            if(roll >= 10) {
                successes += 2;
            }
            else if(roll >= 6) {
                successes++;
            }
        }

        var rollResultEvent = new RollResultEvent(name, attribute, skill, attributeDie, skillDie, diceRolls, rollTypes, successes);
        messageSenderService.sendRollResultEvent(rollResultEvent);
    }
}
