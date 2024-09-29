package com.cthulhu.events;

import com.cthulhu.enums.RollType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RollResultEvent extends Event {
    private List<Integer> diceRolls;
    private List<RollType> rollTypes;
    private int successes;
}
