package com.coca.server.events;

import com.coca.server.enums.RollType;
import com.coca.server.models.RollResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RollResultEvent extends Event {
    private String bladeRunnerName;
    private String attribute;
    private String skill;
    private int attributeValue;
    private int skillValue;
    private List<Integer> diceRolls;
    private List<RollType> rollTypes;
    private int successes;
    private boolean canPush;

    public RollResultEvent(String name, RollResult rollResult) {
        bladeRunnerName = name;
        attribute = rollResult.getAttribute();
        skill = rollResult.getSkill();
        attributeValue = rollResult.getAttributeValue();
        skillValue = rollResult.getSkillValue();
        diceRolls = rollResult.getDiceRolls();
        rollTypes = rollResult.getRollTypes();
        successes = rollResult.getSuccesses();
        canPush = rollResult.isCanPush();
    }
}
