package com.cthulhu.listeners;

import com.cthulhu.annotations.CoCaListener;
import com.cthulhu.events.CustomEvent;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

@CoCaListener(handles = CustomEvent.class)
public class CustomListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

    }
}
