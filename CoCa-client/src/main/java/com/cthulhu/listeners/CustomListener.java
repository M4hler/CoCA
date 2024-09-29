package com.cthulhu.listeners;

import com.cthulhu.events.Event;
import lombok.Setter;

import java.util.function.Consumer;

public abstract class CustomListener<T extends Event> {
    @Setter
    protected Consumer<T> hook;

    public void handleRequest(Event event) {
        handle((T) event);
    }

    public void acceptHook(T parameter) {
        if(hook != null) {
            hook.accept(parameter);
        }
    }

    protected abstract void handle(T event);

}