package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.JoinEvent;

@CoCaListener(handles = JoinEvent.class)
public class JoinEventListener extends CustomListener<JoinEvent>{
    @Override
    protected void handle(JoinEvent event) {

    }
}
