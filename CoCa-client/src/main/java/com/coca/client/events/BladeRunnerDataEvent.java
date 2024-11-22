package com.coca.client.events;

import com.coca.client.models.BladeRunner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BladeRunnerDataEvent extends Event {
    private BladeRunner bladeRunner;
}
