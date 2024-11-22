package com.coca.server.events;

import com.coca.server.models.BladeRunner;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BladeRunnerDataEvent extends Event {
    private final BladeRunner bladeRunner;
}
