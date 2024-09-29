package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.RollResultEvent;

@CoCaListener(handles = RollResultEvent.class)
public class RollResultEventListener extends CustomListener<RollResultEvent> {
    @Override
    protected void handle(RollResultEvent event) {
        System.out.println("Received RollResultEvent");
        acceptHook(event);
    }
}
