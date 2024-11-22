package com.coca.client.listeners;

import com.coca.client.annotations.CoCaListener;
import com.coca.client.events.ShiftChangeResultEvent;

@CoCaListener(handles = ShiftChangeResultEvent.class)
public class ShiftChangeResultEventListener extends CustomListener<ShiftChangeResultEvent> {
    @Override
    protected void handle(ShiftChangeResultEvent event) {
        acceptHook(event);
    }
}
