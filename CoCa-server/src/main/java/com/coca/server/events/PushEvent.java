package com.coca.server.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PushEvent extends Event {
    private int bladeRunnerId;
}
