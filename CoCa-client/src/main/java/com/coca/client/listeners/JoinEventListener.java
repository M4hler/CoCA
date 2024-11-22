package com.coca.client.listeners;

import com.coca.client.annotations.CoCaListener;
import com.coca.client.events.JoinEvent;

@CoCaListener(handles = JoinEvent.class)
public class JoinEventListener extends CustomListener<JoinEvent> {
    @Override
    protected void handle(JoinEvent event) {
        acceptHook(event);
    }
}
