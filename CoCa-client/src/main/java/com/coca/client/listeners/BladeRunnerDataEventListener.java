package com.coca.client.listeners;

import com.coca.client.annotations.CoCaListener;
import com.coca.client.events.BladeRunnerDataEvent;

@CoCaListener(handles = BladeRunnerDataEvent.class)
public class BladeRunnerDataEventListener extends CustomListener<BladeRunnerDataEvent> {
    @Override
    protected void handle(BladeRunnerDataEvent event) {
        if(event.getBladeRunner() != null) {
            acceptHook(event);
        }
    }
}
