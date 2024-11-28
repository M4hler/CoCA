package com.coca.client.listeners;

import com.coca.client.annotations.CoCaListener;
import com.coca.client.events.MainframeAllDataEvent;

@CoCaListener(handles = MainframeAllDataEvent.class)
public class MainframeAllDataEventListener extends CustomListener<MainframeAllDataEvent> {
    @Override
    protected void handle(MainframeAllDataEvent event) {

    }
}
