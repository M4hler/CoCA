package com.coca.server.listeners;

import com.coca.server.events.Event;
import jakarta.jms.Queue;

public abstract class CustomListener<T extends Event> {
    public void handleRequest(Event event, Queue source) {
        handle((T) event, source);
    }

    protected abstract void handle(T event, Queue source);
}
