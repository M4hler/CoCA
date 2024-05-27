package com.cthulhu.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinEvent extends Event {
    private final String name;
}
