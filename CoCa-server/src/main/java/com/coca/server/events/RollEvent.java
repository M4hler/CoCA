package com.coca.server.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RollEvent extends Event {
    private String bladeRunner;
    private String skill;
    private Integer bonusDie;
}
