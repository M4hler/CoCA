package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.ShiftChangeResultEvent;

@CoCaListener(handles = ShiftChangeResultEvent.class)
public class ShiftChangeResultEventListener extends CustomListener<ShiftChangeResultEvent> {
    @Override
    protected void handle(ShiftChangeResultEvent event) {
        acceptHook(event);
    }
}
