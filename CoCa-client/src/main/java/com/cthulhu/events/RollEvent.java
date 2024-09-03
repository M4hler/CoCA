package com.cthulhu.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RollEvent extends Event {
    private Integer die;
    private List<String> bladeRunners;
    private String skill;
    private Integer bonusDie;
}
