package com.cthulhu.events;

import com.cthulhu.models.BladeRunner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BladeRunnerDataEvent extends Event {
    private BladeRunner bladeRunner;
}
