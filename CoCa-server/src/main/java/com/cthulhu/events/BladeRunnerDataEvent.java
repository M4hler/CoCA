package com.cthulhu.events;

import com.cthulhu.models.BladeRunner;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BladeRunnerDataEvent extends Event {
    private final BladeRunner bladeRunner;
}
