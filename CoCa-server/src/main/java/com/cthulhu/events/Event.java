package com.cthulhu.events;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Event {
    protected int messageCode;
}
