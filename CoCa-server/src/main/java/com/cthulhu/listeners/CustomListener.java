package com.cthulhu.listeners;

import com.cthulhu.events.Event;

public abstract class CustomListener<T extends Event> {
    public void handleRequest(Event event) {
        handleRequest(event);
    }

    protected abstract void handle(T event);
}
