package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.JoinEvent;

@CoCaListener(handles = JoinEvent.class)
public class TestListener extends CustomListener<JoinEvent> {
    @Override
    public void handle(JoinEvent event) {

    }
}
