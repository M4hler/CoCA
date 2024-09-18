package com.cthulhu.services;

import com.cthulhu.events.RollResultEvent;
import com.cthulhu.models.enums.RollType;
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

    public void rollDice(int attributeDie, int skillDie, int bonusDie) {
        var diceRolls = new ArrayList<Integer>();
        var rollTypes = new ArrayList<RollType>();
        switch (bonusDie) {
            case -1 -> {
                var max = Math.max(attributeDie, skillDie);
                var roll = generatorService.rollDie(max);
                System.out.println("Roll result: " + roll);
                diceRolls.add(roll);
                rollTypes.add(RollType.ATTRIBUTE);
            }
            case 0 -> {
                var attributeRoll = generatorService.rollDie(attributeDie);
                var skillRoll = generatorService.rollDie(skillDie);
                System.out.println("Roll result: " + attributeRoll + " " + skillRoll);
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
                System.out.println("Roll result: " + attributeRoll + " " + skillRoll + " " + bonusRoll);
                diceRolls.add(attributeRoll);
                diceRolls.add(skillRoll);
                diceRolls.add(bonusRoll);
                rollTypes.add(RollType.ATTRIBUTE);
                rollTypes.add(RollType.SKILL);
                rollTypes.add(RollType.BONUS);
            }
        }

        var rollResultEvent = new RollResultEvent(diceRolls, rollTypes);
        messageSenderService.sendRollResultEvent(rollResultEvent);
    }
}
