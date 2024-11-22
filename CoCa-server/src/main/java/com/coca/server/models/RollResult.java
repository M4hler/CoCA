package com.coca.server.models;

import com.coca.server.enums.RollType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RollResult {
    private String attribute;
    private String skill;
    private int attributeValue;
    private int skillValue;
    private List<Integer> diceRolls;
    private List<Integer> diceSizes;
    private List<RollType> rollTypes;
    private int successes;
    private boolean canPush;
}
