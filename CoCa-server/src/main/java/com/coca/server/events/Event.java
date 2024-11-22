package com.coca.server.events;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Event {
    protected int messageCode;
}
