package com.coca.client.listeners;

import com.coca.client.events.RollResultEvent;
import com.coca.client.annotations.CoCaListener;

@CoCaListener(handles = RollResultEvent.class)
public class RollResultEventListener extends CustomListener<RollResultEvent> {
    @Override
    protected void handle(RollResultEvent event) {
        acceptHook(event);
    }
}
