package com.coca.client.events;

import com.coca.client.enums.RollType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RollResultEvent extends Event {
    private int bladeRunnerId;
    private String attribute;
    private String skill;
    private int attributeValue;
    private int skillValue;
    private List<Integer> diceRolls;
    private List<RollType> rollTypes;
    private int successes;
    private boolean canPush;
}