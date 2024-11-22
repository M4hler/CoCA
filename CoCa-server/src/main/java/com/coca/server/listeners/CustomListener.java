package com.coca.server.listeners;

import com.coca.server.events.Event;

public abstract class CustomListener<T extends Event> {
    public void handleRequest(Event event) {
        handle((T) event);
    }

    protected abstract void handle(T event);
}
