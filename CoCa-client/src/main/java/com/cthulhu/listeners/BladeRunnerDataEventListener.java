package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.BladeRunnerDataEvent;

@CoCaListener(handles = BladeRunnerDataEvent.class)
public class BladeRunnerDataEventListener extends CustomListener<BladeRunnerDataEvent> {
    @Override
    protected void handle(BladeRunnerDataEvent event) {
        if(event.getBladeRunner() != null) {
            acceptHook(event);
        }
    }
}
