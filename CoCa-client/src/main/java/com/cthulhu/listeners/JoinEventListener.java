package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.JoinEvent;
import lombok.Setter;

import java.util.function.Consumer;

@CoCaListener(handles = JoinEvent.class)
public class JoinEventListener extends CustomListener<JoinEvent> {
    @Setter
    private Consumer<String> hook;

    @Override
    protected void handle(JoinEvent event) {
        System.out.println("Handling request in JoinEventListener");
        if(hook != null) {
            hook.accept(event.getName());
        }
    }
}
